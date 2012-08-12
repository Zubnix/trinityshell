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

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.PointerLeaveNotifyEvent;
import org.trinity.foundation.display.api.event.PointerVisitationNotifyEvent;
import org.trinity.render.paintengine.qt.impl.QFRenderEventConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>QFusionMouseLeaveEventConverter</code> takes a <code>QEvent</code>
 * and it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>MouseEnterLeaveNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(multiple = true)
@Singleton
public final class QFMouseLeaveConverterImpl implements QFRenderEventConverter {

	private final QEvent.Type qType;

	@Inject
	protected QFMouseLeaveConverterImpl(@Named("Leave") final QEvent.Type qType) {
		this.qType = qType;
	}

	@Override
	public PointerVisitationNotifyEvent convertEvent(	final DisplayEventSource eventSource,
														final QEvent qEvent) {
		return new PointerLeaveNotifyEvent(eventSource);
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
