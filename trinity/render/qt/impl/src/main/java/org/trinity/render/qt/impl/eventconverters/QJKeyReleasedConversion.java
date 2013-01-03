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
package org.trinity.render.qt.impl.eventconverters;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.render.qt.impl.QJRenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QKeyEvent;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>QFusionKeyReleaseConverter</code> takes a <code>QKeyEvent</code> and
 * it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>KeyNotifyEvent</code>.
 */
@Bind(multiple = true)
@Singleton
public class QJKeyReleasedConversion implements QJRenderEventConversion {

	QJKeyReleasedConversion() {
	}

	@Override
	public KeyNotifyEvent convertEvent(	final DisplayEventSource source,
										final QEvent qEvent) {
		qEvent.accept();
		final QKeyEvent keyEvent = (QKeyEvent) qEvent;
		if (keyEvent.isAutoRepeat()) {
			return null;
		}

		final int keyCode = keyEvent.nativeScanCode();
		final int state = keyEvent.nativeModifiers();

		final Key key = new Key(keyCode);
		final InputModifiers inputModifiers = new InputModifiers(state);
		final KeyboardInput input = new KeyboardInput(	Momentum.STOPPED,
														key,
														inputModifiers);

		return new KeyNotifyEvent(	source,
									input);
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.KeyRelease;
	}
}