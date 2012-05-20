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
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.event.base.BaseKeyNotifyEvent;
import org.hydrogen.display.api.input.Momentum;
import org.hydrogen.display.api.input.base.BaseInputModifiers;
import org.hydrogen.display.api.input.base.BaseKey;
import org.hydrogen.display.api.input.base.BaseKeyboardInput;

import com.trolltech.qt.gui.QKeyEvent;

// TODO documentation
/**
 * 
 * A <code>QFusionKeyPressConverter</code> takes a <code>QKeyEvent</code> and
 * it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>KeyNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionKeyPressConverter implements
		QFusionEventConverter<QKeyEvent> {

	@Override
	public KeyNotifyEvent sinkEvent(final DisplayEventSource source,
			final QKeyEvent qEvent) {

		qEvent.accept();
		final QKeyEvent keyEvent = qEvent;

		if (keyEvent.isAutoRepeat()) {
			return null;
		}

		final int detail = keyEvent.nativeScanCode();

		// TODO translate modifiers bitstring to separate modifier keycodes
		final int state = keyEvent.nativeModifiers();

		final BaseKey baseKey = new BaseKey(Integer.valueOf(detail));
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);
		final BaseKeyboardInput input = new BaseKeyboardInput(Momentum.STARTED,
				baseKey, baseInputModifiers);

		return new BaseKeyNotifyEvent(DisplayEventType.KEY_PRESSED, source,
				input);
	}
}