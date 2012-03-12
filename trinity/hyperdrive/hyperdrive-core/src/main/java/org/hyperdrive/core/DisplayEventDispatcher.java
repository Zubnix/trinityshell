/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.core;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.hydrogen.api.display.Display;
import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.event.ConfigureRequestEvent;
import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MapRequestEvent;
import org.hydrogen.api.event.EventHandler;
import org.hydrogen.api.event.EventManager;
import org.hydrogen.api.event.TypeBoundEventHandler;
import org.hydrogen.event.EventBus;
import org.hyperdrive.api.core.ManagedDisplay;

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
final class DisplayEventDispatcher extends EventBus implements Runnable {

	// TODO this is basically the same mechanism as used in EventBus. Find a way
	// to seperate and uniform this mechanism.
	private final Map<DisplayEventSource, List<WeakReference<EventManager>>> eventManagers;

	// TODO move itf to seperate file and create a version for each display
	// event handler in core.api
	private interface DisplayEventHandler<T extends DisplayEvent> extends
			TypeBoundEventHandler<DisplayEventType, T> {
	}

	private final class ConfigureRequestHandler implements
			DisplayEventHandler<ConfigureRequestEvent> {
		@Override
		public void handleEvent(final ConfigureRequestEvent event) {
			handleConfigureRequestEvent(event);
		}

		@Override
		public DisplayEventType getType() {
			return DisplayEventType.CONFIGURE_REQUEST;
		}
	}

	private final class MapRequestEventHandler implements
			DisplayEventHandler<MapRequestEvent> {
		@Override
		public void handleEvent(final MapRequestEvent event) {
			handleMapRequestEvent(event);
		}

		@Override
		public DisplayEventType getType() {
			return DisplayEventType.MAP_REQUEST;
		}
	}

	private final class DefaultDisplayEventHandler implements
			EventHandler<DisplayEvent> {
		@Override
		public void handleEvent(final DisplayEvent event) {
			doDefaultEventHandlingForDisplayEvent(event);
		}
	}

	private static final Logger LOGGER = Logger
			.getLogger(DisplayEventDispatcher.class);
	private static final String DISPLAYEVENT_LOGMESSAGE = "Firing display event: %s";
	private static final String THREADSTART_LOGMESSAGE = "Starting display event dispatching loop.";
	private static final String THREADSTOP_LOGMESSAGE = "Display event dispatching loop terminated.";

	private final Display display;
	private final BaseManagedDisplay managedDisplay;
	private volatile boolean running;

	DisplayEventDispatcher(final BaseManagedDisplay managedDisplay) {
		this.eventManagers = new HashMap<DisplayEventSource, List<WeakReference<EventManager>>>();
		this.managedDisplay = managedDisplay;
		this.display = managedDisplay.getDisplay();
		initEventManagers();
	}

	private BaseManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	private void initEventManagers() {
		addTypedEventHandler(new ConfigureRequestHandler());
		addTypedEventHandler(new MapRequestEventHandler());
		final DefaultDisplayEventHandler defaultDisplayEventHandler = new DefaultDisplayEventHandler();
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.BUTTON_PRESSED);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.BUTTON_RELEASED);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.KEY_PRESSED);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.KEY_RELEASED);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.DESTROY_NOTIFY);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.UNMAP_NOTIFY);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.MOUSE_ENTER);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.MOUSE_LEAVE);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.PROPERTY_CHANGED);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.FOCUS_GAIN_NOTIFY);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.FOCUS_LOST_NOTIFY);
		addEventHandler(defaultDisplayEventHandler, DisplayEventType.MAP_NOTIFY);
		addEventHandler(defaultDisplayEventHandler,
				DisplayEventType.CONFIGURE_NOTIFY);
	}

	private void handleMapRequestEvent(final MapRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	private void handleConfigureRequestEvent(final ConfigureRequestEvent event) {
		createNewClientWhenNoEventManagers(event);
	}

	private void createNewClientWhenNoEventManagers(final DisplayEvent event) {
		final List<WeakReference<EventManager>> eventManagers = this.eventManagers
				.get(event.getEventSource());

		final DisplayEventSource eventSource = event.getEventSource();

		if (eventManagers.isEmpty()
				&& (eventSource instanceof PlatformRenderArea)) {
			// This will create & register the clientwindow if it doesnt exist
			// yet
			getManagedDisplay().getClientWindow(
					(PlatformRenderArea) eventSource);
		}

		doDefaultEventHandlingForDisplayEvent(event);
	}

	@Override
	public void run() {
		DisplayEventDispatcher.LOGGER
				.info(DisplayEventDispatcher.THREADSTART_LOGMESSAGE);

		if (this.running) {
			return;
		}
		this.running = true;
		Thread.currentThread().setName(
				String.format("Hyperdrive %s thread",
						DisplayEventDispatcher.class.getSimpleName()));

		while (this.running) {
			try {
				dispatchNextEvent(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		DisplayEventDispatcher.LOGGER
				.info(DisplayEventDispatcher.THREADSTOP_LOGMESSAGE);
	}

	/**
	 * Fetch and dispatch the next <code>DisplayEvent</code>.
	 * 
	 * @param block
	 *            True to wait for a new event to become available. False to
	 *            continue without dispatching an event.
	 */
	private void dispatchNextEvent(final boolean block) {
		if (!block && this.display.isMasterQueueEmpty()) {
			return;
		}

		final DisplayEvent displayEvent = this.display
				.getEventFromMasterQueue();

		if (displayEvent != null) {

			DisplayEventDispatcher.LOGGER.debug(String.format(
					DisplayEventDispatcher.DISPLAYEVENT_LOGMESSAGE,
					displayEvent, getManagedDisplay()));

			fireEvent(displayEvent);
		}
	}

	void shutDown() {
		this.running = false;
	}

	private void doDefaultEventHandlingForDisplayEvent(final DisplayEvent event) {
		final List<WeakReference<EventManager>> list = this.eventManagers
				.get(event.getEventSource());

		for (final WeakReference<EventManager> eventManagerRef : list) {
			final EventManager eventManager = eventManagerRef.get();
			if (eventManager == null) {
				list.remove(eventManagerRef);
			} else {
				eventManager.fireEvent(event);
			}
		}
	}

	void addEventManagerForDisplayEventSource(final EventManager manager,
			final DisplayEventSource forSource) {
		final WeakReference<EventManager> eventManagerReference = new WeakReference<EventManager>(
				manager);
		if (this.eventManagers.containsKey(forSource)) {
			this.eventManagers.get(forSource).add(eventManagerReference);
		} else {
			final CopyOnWriteArrayList<WeakReference<EventManager>> list = new CopyOnWriteArrayList<WeakReference<EventManager>>();
			list.add(eventManagerReference);
			this.eventManagers.put(forSource, list);
		}
	}
}
