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

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.PlatformRenderArea;
import org.trinity.foundation.display.api.event.ConfigureRequestEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.MapRequestEvent;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.RenderAreaFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * An <code>EventDispatcher</code> fetches and dispatches
 * <code>DisplayEvent</code>s for a {@link ManagedDisplay}. An
 * <code>EventDispatcher</code> fetches <code>DisplayEvent</code>s from the
 * <code>Display</code> that is wrapped by a <code>ManagedDisplay</code>. After
 * an event is fetched, it is delivered at corresponding the
 * <code>EventBus</code> objects that were registered with the
 * <code>ManagedDisplay</code>. (see
 * {@link ManagedDisplay#registerEventBus(DisplayEventSource, EventBus)})
 * <p>
 * The <code>EventDispatcher</code> implements the main hyperdrive even loop in
 * it's run method. This method is activated by the <code>ManagedDisplay</code>
 * that created the <code>EventDispatcher</code>. See
 * {@link ManagedDisplay#start()}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class DisplayEventDispatcher implements Runnable {

	private final EventBus displayEventBus;

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Map<DisplayEventSource, List<WeakReference<EventBus>>> eventManagers;

	private final DisplayServer display;

	private final RenderAreaFactory clientFactory;

	DisplayEventDispatcher(	final EventBus eventBus,
							final DisplayServer display,
							final RenderAreaFactory clientFactory) {
		this.displayEventBus = eventBus;
		this.eventManagers = new WeakHashMap<DisplayEventSource, List<WeakReference<EventBus>>>();
		this.display = display;
		this.clientFactory = clientFactory;
		this.displayEventBus.register(this);
	}

	@Subscribe
	public void handleMapRequestEvent(final MapRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	@Subscribe
	public void handleConfigureRequestEvent(final ConfigureRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	private void createNewClientWhenNoEventManagers(final DisplayEvent event) {
		final List<WeakReference<EventBus>> eventManagersByEventSource = this.eventManagers
				.get(event.getEventSource());

		final DisplayEventSource eventSource = event.getEventSource();

		if (((eventManagersByEventSource == null) || eventManagersByEventSource
				.isEmpty()) && (eventSource instanceof PlatformRenderArea)) {
			this.clientFactory
					.createRenderArea((PlatformRenderArea) eventSource);
		}
		doDefaultEventHandlingForDisplayEvent(event);
	}

	@Override
	public void run() {
		Thread.currentThread()
				.setName(String.format(	"Hyperdrive %s thread",
										DisplayEventDispatcher.class
												.getSimpleName()));

		while (!Thread.interrupted()) {
			try {
				postNextEvent(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
			Thread.yield();
		}
	}

	/**
	 * Fetch and dispatch the next <code>DisplayEvent</code>.
	 * 
	 * @param block
	 *            True to wait for a new event to become available. False to
	 *            continue without dispatching an event.
	 */
	public void postNextEvent(final boolean block) {
		if (!block && this.display.hasNextDisplayEvent()) {
			return;
		}

		final DisplayEvent displayEvent = this.display.getNextDisplayEvent();

		if (displayEvent != null) {
			this.displayEventBus.post(displayEvent);
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

	public void addEventManagerForDisplayEventSource(	final EventBus eventBus,
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
