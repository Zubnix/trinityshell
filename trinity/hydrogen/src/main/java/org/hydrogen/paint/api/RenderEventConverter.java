/*
 * This file is part of Fusion-qtjambi. Fusion-qtjambi is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-qtjambi is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Fusion-qtjambi. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.paint.api;

import org.hydrogen.display.api.event.DisplayEvent;
import org.hydrogen.display.api.event.DisplayEventSource;

/**
 * A <code>QFusionEventConverter</code> converts a <code>QEvent</code>, coming
 * from a <code>QWidget</code> peer, together with it's
 * <code>DisplayEventSource</code> to the corresponding
 * <code>DisplayEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface RenderEventConverter<T, E> {
	/**
	 * @param eventSource
	 *            The object on who's behalf the paint peer works.
	 * @param renderEvent
	 *            The event emitted by the QtJambi paint toolkit
	 * @return The equivalent <code>DisplayEvent</code>
	 */
	DisplayEvent convertEvent(DisplayEventSource eventSource, T renderEvent);

	E getFromType();
}