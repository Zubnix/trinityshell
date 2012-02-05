/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.paintengine;

import org.hydrogen.displayinterface.event.DisplayEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;

import com.trolltech.qt.core.QEvent;

/**
 * A <code>QFusionEventConverter</code> converts a <code>QEvent</code>, coming
 * from a <code>QWidget</code> peer, together with it's
 * <code>DisplayEventSource</code> to the corresponding
 * <code>DisplayEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
interface QFusionEventConverter<T extends QEvent> {
	/**
	 * 
	 * 
	 * @param eventSource
	 *            The object on who's behalf the paint peer works.
	 * @param qEvent
	 *            The event emitted by the QtJambi paint toolkit
	 * @return The equivalent <code>DisplayEvent</code>
	 * 
	 */
	DisplayEvent sinkEvent(DisplayEventSource eventSource, T qEvent);
}