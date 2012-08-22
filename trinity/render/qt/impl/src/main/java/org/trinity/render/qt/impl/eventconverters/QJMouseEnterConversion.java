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
package org.trinity.render.qt.impl.eventconverters;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.PointerEnterNotifyEvent;
import org.trinity.foundation.display.api.event.PointerVisitationNotifyEvent;
import org.trinity.render.qt.impl.QJRenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>QFusionMouseEnterEventConverter</code> takes a <code>QEvent</code>
 * and it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>MouseEnterLeaveNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(multiple = true)
@Singleton
public class QJMouseEnterConversion implements QJRenderEventConversion {

	QJMouseEnterConversion() {
	}

	@Override
	public PointerVisitationNotifyEvent convertEvent(	final DisplayEventSource eventSource,
														final QEvent qEvent) {
		return new PointerEnterNotifyEvent(eventSource);
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.Enter;
	}
}
