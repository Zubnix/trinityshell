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
package org.trinity.render.paintengine.qt.impl.eventconverters;

import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.MouseVisitationNotifyEvent;
import org.trinity.render.paintengine.qt.api.QFRenderEventConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

// TODO documentation
/**
 * A <code>QFusionMouseLeaveEventConverter</code> takes a <code>QEvent</code>
 * and it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>MouseEnterLeaveNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public final class QFMouseLeaveConverterImpl implements QFRenderEventConverter {

	private final DisplayEventFactory displayEventFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFMouseLeaveConverterImpl(@Named("Leave") final QEvent.Type qType,
										final DisplayEventFactory displayEventFactory) {
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public MouseVisitationNotifyEvent convertEvent(	final DisplayEventSource eventSource,
													final QEvent qEvent) {
		return this.displayEventFactory.createMouseLeave(eventSource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.qt.paintengine.QFusionEventConverter#getFromType()
	 */
	@Override
	public Type getFromType() {
		return this.qType;
	}
}
