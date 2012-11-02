/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.api;

import org.trinity.foundation.display.api.event.DisplayEvent;

/**
 * An <code>EventProducer</code> is a production source of
 * <code>DisplayEvent</code>s. It does not make {@link DisplayEvent}s available
 * directly but makes sure that they are placed on the {@link DisplayServer}'s
 * event queue.
 */
public interface DisplayEventProducer {

	/***************************************
	 * Start constructing {@link DisplayEvent}s and place them on the
	 * {@link DisplayServer}'s event queue.
	 *************************************** 
	 */
	void startDisplayEventProduction();

	/***************************************
	 * Halt the production of {@link DisplayEvent}s.
	 * 
	 * @see #startDisplayEventProduction()
	 *************************************** 
	 */
	void stopDisplayEventProduction();
}
