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
package org.hydrogen.api.event;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public interface EventHandlerManager {

	/**
	 * 
	 * @param <T>
	 * @param eventHandler
	 * @param type
	 */
	<T extends Type> void addEventHandler(
			EventHandler<? extends Event<T>> eventHandler, T type);

	/**
	 * 
	 * @param typedEventHandler
	 */
	<T extends Type> void addTypedEventHandler(
			TypeBoundEventHandler<T, ? extends Event<T>> typedEventHandler);

	/**
	 * 
	 * @param eventHandler
	 */
	<T extends Type> void removeEventHandler(
			EventHandler<? extends Event<T>> eventHandler, T type);

	/**
	 * 
	 * @param typedEventHandler
	 */
	<T extends Type> void removeTypedEventHandler(
			TypeBoundEventHandler<T, ? extends Event<T>> typedEventHandler);
}
