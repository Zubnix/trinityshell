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

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.display.event.PointerEnterNotify;
import org.trinity.foundation.api.display.event.PointerVisitationNotify;
import org.trinity.foundation.render.qt.impl.RenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;

// TODO documentation
/**
 * A <code>QFusionMouseEnterEventConverter</code> takes a <code>QEvent</code>
 * and it's <code>DisplayEventTarget</code> as input and converts it to a
 * <code>MouseEnterLeaveNotifyEvent</code>.
 *
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(multiple = true)
@Singleton
public class MouseEnterConversion implements RenderEventConversion {

	MouseEnterConversion() {
	}

	@Override
	public PointerVisitationNotify convertEvent(final Object view,
												final QObject evenetProducer,
												final QEvent qEvent) {
		return new PointerEnterNotify();
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.Enter;
	}
}
