/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.core.event.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


// TODO documentation
/**
 * An <code>EventBus</code> provides a way for listeners to be notified of
 * <code>Event</code>s that are fired by the <code>EventBus</code> itself.
 * <p>
 * Listeners come in the form of an {@link EventHandler}. The
 * <code>EventHandler</code> is registered with the <code>EventBus</code>
 * together with the <code>Type</code> of the <code>Event</code> it wants to be
 * notified of.
 * <p>
 * When an <code>Event</code> is fired by the <code>EventBus</code> it first
 * looks up the <code>Type</code> of the </code>Event</code> together with all
 * the matching <code>EventHanlders</code> that were registered. The
 * <code>EventBus</code> then calls for each matching <code>EventHandler</code>
 * the </code>handleEvent</code> method.
 * <p>
 * <code>EventHandlers</code> are called in the order they were added, unless
 * the <code>EventHandler</code> was registered with the index argument in which
 * case the index argument will determine when the <code>EventHanlder</code>
 * will be called.
 * <p>
 * A registered <code>EventHandler</code> can be removed in which case it will
 * no longer be called by the respective <code>EventBus</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
@SuppressWarnings("rawtypes")
// rawtypes because javac has bugs when it comes to generic method
// declarations (unlike eclipse compiler).
public class EventBus implements EventHandlerManager, EventManager {

	private final Map<Type, List<EventHandler>> eventHandlerSwitch;

	/**
	 * 
	 */
	public EventBus() {
		this.eventHandlerSwitch = new ConcurrentHashMap<Type, List<EventHandler>>();
	}

	@Override
	public <T extends Type> void addEventHandler(
			final EventHandler<? extends Event<T>> eventHandler, final T type) {
		this.addEventHandler(eventHandler, type, -1);
	}

	/**
	 * 
	 * @param eventHandler
	 * @param type
	 * @param index
	 */
	public <T extends Type> void addEventHandler(
			final EventHandler<? extends Event<T>> eventHandler, final T type,
			final int index) {

		final List<EventHandler> eventHandlersList;

		if (this.eventHandlerSwitch.containsKey(type)) {
			eventHandlersList = this.eventHandlerSwitch.get(type);
		} else {
			eventHandlersList = new CopyOnWriteArrayList<EventHandler>();
			this.eventHandlerSwitch.put(type, eventHandlersList);
		}

		if (index < 0) {
			eventHandlersList.add(eventHandler);
		} else {
			eventHandlersList.add(index, eventHandler);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fireEvent(final Event<? extends Type> event) {
		if (this.eventHandlerSwitch.containsKey(event.getType())) {
			final List<EventHandler> eventHandlersList = this.eventHandlerSwitch
					.get(event.getType());

			for (final EventHandler eventHandler : eventHandlersList) {
				eventHandler.handleEvent(event);
			}
		}
	}

	@Override
	public <T extends Type> void removeEventHandler(
			final EventHandler<? extends Event<T>> toBeDeletedEventHandler,
			final T type) {
		final List<EventHandler> eventHandlersList = this.eventHandlerSwitch
				.get(type);
		eventHandlersList.remove(toBeDeletedEventHandler);
	}

	@Override
	public <T extends Type> void addTypedEventHandler(
			final TypeBoundEventHandler<T, ? extends Event<T>> typedEventHandler) {
		addEventHandler(typedEventHandler, typedEventHandler.getType());
	}

	@Override
	public <T extends Type> void removeTypedEventHandler(
			final TypeBoundEventHandler<T, ? extends Event<T>> typedEventHandler) {
		removeEventHandler(typedEventHandler, typedEventHandler.getType());
	}
}