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

// TODO documentation
/**
 * An <code>EventHandler</code> provides the interface for objects wanting to be
 * notified of <code>Event</code>s that are fired by an
 * <code>EventManager</code>, like the <code>EventBus</code>.
 * <p>
 * Before an </code>EventHandler</code> can be notified of <code>Event</code>s,
 * it must first register itself with an <code>EventHandlerManager</code>, like
 * the <code>EventBus</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @param <T>
 *            The <code>Event</code> that the <code>EventHandler</code> will
 *            handle.
 */
public interface EventHandler<T extends Event<? extends Type>> {

	/**
	 * 
	 * @param event
	 */
	void handleEvent(T event);
}
