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
package org.trinity.foundation.render.qt.impl.eventconverters;

import org.trinity.foundation.api.display.event.FocusLostNotifyEvent;
import org.trinity.foundation.api.display.event.FocusNotifyEvent;
import org.trinity.foundation.render.qt.impl.QJRenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>QFusionDestroyConverter</code> takes a <code>QFocusEvent</code> and
 * it's <code>DisplayEventTarget</code> as input and converts it to a
 * <code>FocusNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(multiple = true)
@Singleton
public class QJFocusLostConversion implements QJRenderEventConversion {

	QJFocusLostConversion() {
	}

	@Override
	public FocusNotifyEvent convertEvent(	final Object eventTarget,
											final Object view,
											final QObject eventProducer,
											final QEvent qEvent) {
		return new FocusLostNotifyEvent(eventTarget);
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.FocusOut;
	}
}