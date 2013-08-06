/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.render.qt.impl.eventconverters;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.display.event.FocusLostNotify;
import org.trinity.foundation.api.display.event.FocusNotify;
import org.trinity.foundation.render.qt.impl.RenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;

/**
 * A <code>QFusionDestroyConverter</code> takes a <code>QFocusEvent</code> and
 * it's <code>DisplayEventTarget</code> as input and converts it to a
 * <code>FocusNotify</code>.
 *
 * @author Erik De Rijcke
 * @since 1.0
 */
@Deprecated
@Bind(multiple = true)
@Singleton
public class FocusLostConversion implements RenderEventConversion {

	FocusLostConversion() {
	}

	@Override
	public FocusNotify convertEvent(final Object view,
									final QObject eventProducer,
									final QEvent qEvent) {
		return new FocusLostNotify();
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.FocusOut;
	}
}