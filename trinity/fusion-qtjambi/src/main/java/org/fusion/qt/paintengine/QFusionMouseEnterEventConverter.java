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

import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.display.api.event.base.BaseMouseEnterNotifyEvent;

import com.trolltech.qt.core.QEvent;

// TODO documentation
/**
 * 
 * A <code>QFusionMouseEnterEventConverter</code> takes a <code>QEvent</code>
 * and it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>MouseEnterLeaveNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionMouseEnterEventConverter implements
		QFusionEventConverter<QEvent> {

	@Override
	public MouseEnterLeaveNotifyEvent sinkEvent(
			final DisplayEventSource eventSource, final QEvent qEvent) {
		return new BaseMouseEnterNotifyEvent(eventSource);
	}

}
