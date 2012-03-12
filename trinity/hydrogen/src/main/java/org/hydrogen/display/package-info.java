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

// TODO documentation
/**
 * Groups a number of interfaces and base class
 * implementations that provide the contract to talk to a native display
 * server.
 * <p>
 * At the center lies the {@link org.hydrogen.api.display.Display}
 * interface. A <code>Display</code> implementation queues
 * {@link org.hydrogen.api.display.event.DisplayEvent}s to notify of the
 * state and affairs of the underlying native display. These events are defined in the
 * <code>org.hydrogen.displayinterface.event</code> package.
 * <p>
 */
package org.hydrogen.display;