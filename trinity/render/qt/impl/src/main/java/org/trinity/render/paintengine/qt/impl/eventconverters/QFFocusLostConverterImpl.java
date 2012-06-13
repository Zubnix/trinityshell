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

import org.trinity.foundation.display.api.event.DisplayEventFactory;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.FocusNotifyEvent;
import org.trinity.render.paintengine.qt.api.QFRenderEventConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

/**
 * A <code>QFusionDestroyConverter</code> takes a <code>QFocusEvent</code> and
 * it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>FocusNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public final class QFFocusLostConverterImpl implements QFRenderEventConverter {

	private final DisplayEventFactory displayEventFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFFocusLostConverterImpl(	@Named("FocusOut") final QEvent.Type qType,
										final DisplayEventFactory displayEventFactory) {
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public FocusNotifyEvent convertEvent(	final DisplayEventSource eventSource,
											final QEvent qEvent) {
		return this.displayEventFactory.createFocusLost(eventSource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.qt.paintengine.QFusionEventConverter#getFromType()
	 */
	@Override
	public Type getQEventType() {
		return this.qType;
	}
}