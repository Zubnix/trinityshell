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

import org.hydrogen.displayinterface.event.BaseDestroyNotifyEvent;
import org.hydrogen.displayinterface.event.DestroyNotifyEvent;
import org.hydrogen.displayinterface.event.DisplayEventSource;

import com.trolltech.qt.core.QEvent;

/**
 * A <code>QFusionDestroyConverter</code> takes a <code>QEvent</code> and it's
 * <code>DisplayEventSource</code> as input and converts it to a
 * <code>DestroyNotifyEvent</code>.
 * 
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public final class QFusionDestroyConverter implements
		QFusionEventConverter<QEvent> {

	@Override
	public DestroyNotifyEvent sinkEvent(final DisplayEventSource eventSource,
			final QEvent qEvent) {
		return new BaseDestroyNotifyEvent(eventSource);
	}
}
