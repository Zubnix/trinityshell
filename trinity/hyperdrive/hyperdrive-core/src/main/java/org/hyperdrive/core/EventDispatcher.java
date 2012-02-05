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

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.hydrogen.displayinterface.Display;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.event.ConfigureRequestEvent;
import org.hydrogen.displayinterface.event.DestroyNotifyEvent;
import org.hydrogen.displayinterface.event.DisplayEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.displayinterface.event.FocusNotifyEvent;
import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.event.MapRequestEvent;
import org.hydrogen.displayinterface.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.displayinterface.event.PropertyChangedNotifyEvent;
import org.hydrogen.displayinterface.event.UnmappedNotifyEvent;
import org.hydrogen.eventsystem.Event;
import org.hydrogen.eventsystem.EventBus;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.eventsystem.Type;

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
 * {@link ManagedDisplay#startEventDispatcher()}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class EventDispatcher implements Runnable {

	private static final Logger LOGGER = Logger
			.getLogger(EventDispatcher.class);
	private static final String DISPLAYEVENT_LOGMESSAGE = "Firing display event: %s";
	private static final String THREADSTART_LOGMESSAGE = "Starting display event dispatching loop.";
	private static final String THREADSTOP_LOGMESSAGE = "Display event dispatching loop terminated.";

	private final Display display;
	private final ManagedDisplay managedDisplay;
	private volatile boolean running;

	EventDispatcher(final ManagedDisplay managedDisplay) {
		this.managedDisplay = managedDisplay;
		this.display = managedDisplay.getDisplay();
		initEventManagers();
	}

	private ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	private void initEventManagers() {
		getManagedDisplay().addEventHandler(
				new EventHandler<ConfigureRequestEvent>() {
					@Override
					public void handleEvent(final ConfigureRequestEvent event) {
						EventDispatcher.this.handleConfigureRequestEvent(event);
					}
				}, ConfigureRequestEvent.TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<MapRequestEvent>() {
					@Override
					public void handleEvent(final MapRequestEvent event) {
						EventDispatcher.this.handleMapRequestEvent(event);
					}
				}, MapRequestEvent.TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<ButtonNotifyEvent>() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						EventDispatcher.this.handleButtonPressed(event);
					}
				}, ButtonNotifyEvent.PRESSED_TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<ButtonNotifyEvent>() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						EventDispatcher.this.handleButtonReleased(event);
					}
				}, ButtonNotifyEvent.RELEASED_TYPE);
		getManagedDisplay().addEventHandler(new EventHandler<KeyNotifyEvent>() {
			@Override
			public void handleEvent(final KeyNotifyEvent event) {
				EventDispatcher.this.handleKeyPressed(event);
			}
		}, KeyNotifyEvent.KEY_PRESSED);
		getManagedDisplay().addEventHandler(new EventHandler<KeyNotifyEvent>() {
			@Override
			public void handleEvent(final KeyNotifyEvent event) {
				EventDispatcher.this.handleKeyReleased(event);
			}
		}, KeyNotifyEvent.KEY_RELEASED);
		getManagedDisplay().addEventHandler(
				new EventHandler<DestroyNotifyEvent>() {
					@Override
					public void handleEvent(final DestroyNotifyEvent event) {
						EventDispatcher.this.handleDestroyed(event);
					}
				}, DestroyNotifyEvent.TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<UnmappedNotifyEvent>() {
					@Override
					public void handleEvent(final UnmappedNotifyEvent event) {
						EventDispatcher.this.handleUnmapNotify(event);
					}
				}, UnmappedNotifyEvent.TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<MouseEnterLeaveNotifyEvent>() {
					@Override
					public void handleEvent(
							final MouseEnterLeaveNotifyEvent event) {
						EventDispatcher.this.handleMouseEnterNotify(event);
					}
				}, MouseEnterLeaveNotifyEvent.ENTER_TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<MouseEnterLeaveNotifyEvent>() {
					@Override
					public void handleEvent(
							final MouseEnterLeaveNotifyEvent event) {
						EventDispatcher.this.handleMouseLeaveNotify(event);
					}
				}, MouseEnterLeaveNotifyEvent.LEAVE_TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<PropertyChangedNotifyEvent>() {
					@Override
					public void handleEvent(
							final PropertyChangedNotifyEvent event) {
						handlePropertyChangedNotify(event);
					}
				}, PropertyChangedNotifyEvent.TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<FocusNotifyEvent>() {
					@Override
					public void handleEvent(final FocusNotifyEvent event) {
						handleFocusNotify(event);
					}
				}, FocusNotifyEvent.IN_TYPE);
		getManagedDisplay().addEventHandler(
				new EventHandler<FocusNotifyEvent>() {
					@Override
					public void handleEvent(final FocusNotifyEvent event) {
						handleFocusNotify(event);
					}
				}, FocusNotifyEvent.OUT_TYPE);
	}

	private void handleFocusNotify(final FocusNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handlePropertyChangedNotify(
			final PropertyChangedNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleMouseLeaveNotify(final MouseEnterLeaveNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleMouseEnterNotify(final MouseEnterLeaveNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleUnmapNotify(final UnmappedNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleDestroyed(final DestroyNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleKeyReleased(final KeyNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleKeyPressed(final KeyNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleButtonReleased(final ButtonNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleButtonPressed(final ButtonNotifyEvent event) {
		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleMapRequestEvent(final MapRequestEvent event) {
		final Collection<EventBus> eventConductors = getEventConductorsForEventSource(event);

		final DisplayEventSource eventSource = event.getEventSource();
		if (eventConductors.isEmpty()
				&& (eventSource instanceof PlatformRenderArea)) {
			final ClientWindow newclientWindow = new ClientWindow(
					getManagedDisplay(), (PlatformRenderArea) eventSource);
			newclientWindow.syncGeoToPlatformRenderAreaGeo();
			eventConductors.add(newclientWindow);
		}

		doDefaultEventHandlingForDisplayEvent(event);
	}

	private void handleConfigureRequestEvent(final ConfigureRequestEvent event) {
		final Collection<EventBus> eventConductors = getEventConductorsForEventSource(event);

		final DisplayEventSource eventSource = event.getEventSource();

		if (eventConductors.isEmpty()
				&& (eventSource instanceof PlatformRenderArea)) {
			final ClientWindow newClientWindow = new ClientWindow(
					getManagedDisplay(), (PlatformRenderArea) eventSource);
			eventConductors.add(newClientWindow);
		}

		doDefaultEventHandlingForDisplayEvent(event);
	}

	@Override
	public void run() {
		LOGGER.info(THREADSTART_LOGMESSAGE);

		if (this.running) {
			return;
		}
		this.running = true;
		Thread.currentThread().setName(
				String.format("Hyperdrive %s thread",
						EventDispatcher.class.getSimpleName()));

		while (this.running) {
			try {
				dispatchNextEvent(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		LOGGER.info(THREADSTOP_LOGMESSAGE);
	}

	/**
	 * Fetch and dispatch the next <code>DisplayEvent</code>.
	 * 
	 * @param block
	 *            True to wait for a new event to become available. False to
	 *            continue without dispatching an event.
	 */
	public void dispatchNextEvent(final boolean block) {
		if (!block && this.display.isMasterQueueEmpty()) {
			return;
		}

		final DisplayEvent displayEvent = this.display
				.getEventFromMasterQueue();

		if (displayEvent != null) {

			LOGGER.debug(String.format(DISPLAYEVENT_LOGMESSAGE, displayEvent,
					getManagedDisplay()));

			getManagedDisplay().fireEvent(displayEvent);
		}
	}

	void shutDown() {
		this.running = false;
	}

	private LinkedHashSet<EventBus> getEventConductorsForEventSource(
			final DisplayEvent event) {
		final LinkedHashSet<EventBus> eventBusses = getManagedDisplay()
				.getEventConductors(event.getEventSource());
		return eventBusses;
	}

	private void conductEvent(final Collection<EventBus> eventConductors,
			final Event<? extends Type> event) {
		for (final EventBus eventConductor : eventConductors) {
			this.conductEvent(eventConductor, event);
		}
	}

	private void conductEvent(final EventBus eventBus,
			final Event<? extends Type> event) {
		if (eventBus != null) {
			eventBus.fireEvent(event);
		}
	}

	private void doDefaultEventHandlingForDisplayEvent(final DisplayEvent event) {
		this.conductEvent(getEventConductorsForEventSource(event), event);
	}
}
