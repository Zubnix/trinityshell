/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.core.impl;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.GeometryRequestEvent;
import org.trinity.foundation.display.api.event.ShowRequestEvent;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * An <code>EventDispatcher</code> fetches and dispatches
 * <code>DisplayEvent</code>s for a {@link ShellDisplay}. An
 * <code>EventDispatcher</code> fetches <code>DisplayEvent</code>s from the
 * <code>Display</code> that is wrapped by a <code>ManagedDisplay</code>. After
 * an event is fetched, it is delivered at corresponding the
 * <code>EventBus</code> objects that were registered with the
 * <code>ManagedDisplay</code>. (see
 * {@link ShellDisplay#registerEventBus(DisplayEventSource, EventBus)})
 * <p>
 * The <code>EventDispatcher</code> implements the main hyperdrive even loop in
 * it's run method. This method is activated by the <code>ManagedDisplay</code>
 * that created the <code>EventDispatcher</code>. See
 * {@link ShellDisplay#start()}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class ShellDisplayEventDispatcherImpl implements
		ShellDisplayEventDispatcher {

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Map<DisplayEventSource, List<WeakReference<EventBus>>> eventManagers = new WeakHashMap<DisplayEventSource, List<WeakReference<EventBus>>>();
	private final DisplayServer display;
	private final ShellClientFactory shellClientFactory;
	private final EventBus shellEventBus;

	@Inject
	ShellDisplayEventDispatcherImpl(@Named("shellEventBus") final EventBus eventBus,
									final ShellClientFactory shellClientFactory,
									final DisplayServer display) {
		this.shellEventBus = eventBus;
		this.shellClientFactory = shellClientFactory;
		this.display = display;
		this.shellEventBus.register(this);
	}

	@Subscribe
	public void handleMapRequestEvent(final ShowRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	@Subscribe
	public void handleConfigureRequestEvent(final GeometryRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	private void createNewClientWhenNoEventManagers(final DisplayEvent event) {
		final List<WeakReference<EventBus>> eventManagersByEventSource = this.eventManagers
				.get(event.getEventSource());

		final DisplayEventSource eventSource = event.getEventSource();

		if (((eventManagersByEventSource == null) || eventManagersByEventSource
				.isEmpty()) && (eventSource instanceof DisplayRenderArea)) {

			this.shellClientFactory
					.createShellClient((DisplayRenderArea) eventSource);
		}
		// FIXME this may cause shell clients to receive events double
		doDefaultEventHandlingForDisplayEvent(event);
	}

	/**
	 * Fetch and dispatch the next <code>DisplayEvent</code>.
	 * 
	 * @param block
	 *            True to wait for a new event to become available. False to
	 *            continue without dispatching an event.
	 */
	@Override
	public void dispatchDisplayEvent(final boolean block) {
		if (!block && this.display.hasNextDisplayEvent()) {
			return;
		}

		final DisplayEvent displayEvent = this.display.getNextDisplayEvent();

		if (displayEvent != null) {
			this.shellEventBus.post(displayEvent);
		}
	}

	@Subscribe
	public void doDefaultEventHandlingForDisplayEvent(final DisplayEvent event) {
		final List<WeakReference<EventBus>> list = this.eventManagers.get(event
				.getEventSource());

		for (final WeakReference<EventBus> eventManagerRef : list) {
			final EventBus eventManager = eventManagerRef.get();
			if (eventManager == null) {
				list.remove(eventManagerRef);
			} else {
				eventManager.post(event);
			}
		}
	}

	@Override
	public void registerDisplayEventSource(	final EventBus eventBus,
											final DisplayEventSource forSource) {
		final WeakReference<EventBus> eventManagerReference = new WeakReference<EventBus>(eventBus);
		if (this.eventManagers.containsKey(forSource)) {
			this.eventManagers.get(forSource).add(eventManagerReference);
		} else {
			final CopyOnWriteArrayList<WeakReference<EventBus>> list = new CopyOnWriteArrayList<WeakReference<EventBus>>();
			list.add(eventManagerReference);
			this.eventManagers.put(forSource, list);
		}
	}
}
