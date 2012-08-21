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
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.event.ShellSurfaceCreatedEvent;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
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
public class ShellEventDispatcherImpl implements ShellDisplayEventDispatcher {

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Map<DisplayEventSource, List<WeakReference<EventBus>>> eventRecipients = new WeakHashMap<DisplayEventSource, List<WeakReference<EventBus>>>();
	private final DisplayServer display;
	private final Provider<ShellClientSurface> shellClientProvider;
	private final EventBus shellEventBus;

	@Inject
	ShellEventDispatcherImpl(	@Named("shellEventBus") final EventBus shellEventBus,
								final Provider<ShellClientSurface> shellClientProvider,
								final DisplayServer display) {
		this.shellEventBus = shellEventBus;
		this.shellClientProvider = shellClientProvider;
		this.display = display;
		this.shellEventBus.register(this);
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
			newShellClientIfNeeded(displayEvent);
			this.shellEventBus.post(displayEvent);
		}
	}

	private void newShellClientIfNeeded(final DisplayEvent event) {
		final List<WeakReference<EventBus>> eventManagersByEventSource = this.eventRecipients
				.get(event.getEventSource());

		final DisplayEventSource eventSource = event.getEventSource();

		if (((eventManagersByEventSource == null) || eventManagersByEventSource
				.isEmpty()) && (eventSource instanceof DisplayRenderArea)) {

			createShellClient((DisplayRenderArea) eventSource);
		}
	}

	private void createShellClient(final DisplayRenderArea clientDisplayRenderArea) {
		final ShellClientSurface clientShellRenderArea = this.shellClientProvider
				.get();
		clientShellRenderArea.setDisplayRenderArea(clientDisplayRenderArea);
		this.shellEventBus
				.post(new ShellSurfaceCreatedEvent(clientShellRenderArea));
	}

	@Override
	public void registerDisplayEventSource(	final EventBus nodeEventBus,
											final DisplayEventSource forSource) {
		final WeakReference<EventBus> eventManagerReference = new WeakReference<EventBus>(nodeEventBus);
		if (this.eventRecipients.containsKey(forSource)) {
			this.eventRecipients.get(forSource).add(eventManagerReference);
		} else {
			final CopyOnWriteArrayList<WeakReference<EventBus>> list = new CopyOnWriteArrayList<WeakReference<EventBus>>();
			list.add(eventManagerReference);
			this.eventRecipients.put(forSource, list);
		}
	}
}
