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
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.event.base.DisplayEventFactory;
import org.hydrogen.display.api.input.InputModifiers;
import org.hydrogen.display.api.input.Key;
import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.display.api.input.Momentum;
import org.hydrogen.display.api.input.base.BaseInputModifiers;
import org.hydrogen.display.api.input.base.BaseKey;
import org.hydrogen.display.api.input.base.BaseKeyboardInput;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QKeyEvent;

// TODO documentation
/**
 * A <code>QFusionKeyReleaseConverter</code> takes a <code>QKeyEvent</code> and
 * it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>KeyNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class QFusionKeyReleasedConverter implements
		QFusionEventConverter<QKeyEvent> {

	private final DisplayEventFactory displayEventFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFusionKeyReleasedConverter(	@Named("QKeyRelease") final QEvent.Type qType,
											final DisplayEventFactory displayEventFactory) {
		this.displayEventFactory = displayEventFactory;
		this.qType = qType;
	}

	@Override
	public KeyNotifyEvent sinkEvent(final DisplayEventSource source,
									final QKeyEvent qEvent) {
		qEvent.accept();
		final QKeyEvent keyEvent = qEvent;
		if (keyEvent.isAutoRepeat()) {
			return null;
		}

		final int detail = keyEvent.nativeScanCode();
		final int state = keyEvent.nativeModifiers();

		final Key key = new BaseKey(Integer.valueOf(detail));
		final InputModifiers inputModifiers = new BaseInputModifiers(state);
		final KeyboardInput input = new BaseKeyboardInput(	Momentum.STOPPED,
															key,
															inputModifiers);

		return this.displayEventFactory.createKeyReleased(source, input);
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