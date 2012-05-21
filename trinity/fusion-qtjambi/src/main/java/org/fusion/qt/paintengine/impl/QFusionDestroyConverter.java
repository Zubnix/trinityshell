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
package org.fusion.qt.paintengine.impl;

import org.fusion.qt.paintengine.api.QFusionEventConverter;
import org.hydrogen.display.api.event.DestroyNotifyEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.base.DisplayEventFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

/**
 * A <code>QFusionDestroyConverter</code> takes a <code>QEvent</code> and it's
 * <code>DisplayEventSource</code> as input and converts it to a
 * <code>DestroyNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionDestroyConverter implements
		QFusionEventConverter<QEvent> {

	private final DisplayEventFactory displayEventFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFusionDestroyConverter(	@Named("QClose") final QEvent.Type qType,
										final DisplayEventFactory displayEventFactory) {
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public DestroyNotifyEvent sinkEvent(final DisplayEventSource eventSource,
										final QEvent qEvent) {
		return this.displayEventFactory.createDestroyNotify(eventSource);
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
