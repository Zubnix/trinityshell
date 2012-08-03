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
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.InputModifiersFactory;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyFactory;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.KeyboardInputFactory;
import org.trinity.foundation.input.api.event.KeyNotifyEvent;
import org.trinity.render.paintengine.qt.api.QFRenderEventConverter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QKeyEvent;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>QFusionKeyPressConverter</code> takes a <code>QKeyEvent</code> and
 * it's <code>DisplayEventSource</code> as input and converts it to a
 * <code>KeyNotifyEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(multiple = true)
@Singleton
public final class QFKeyPressedConverterImpl implements QFRenderEventConverter {

	private final DisplayEventFactory displayEventFactory;
	private final KeyFactory keyFactory;
	private final InputModifiersFactory inputModifiersFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private final QEvent.Type qType;

	@Inject
	protected QFKeyPressedConverterImpl(@Named("KeyPress") final QEvent.Type qType,
										final DisplayEventFactory displayEventFactory,
										final KeyFactory keyFactory,
										final InputModifiersFactory inputModifiersFactory,
										final KeyboardInputFactory keyboardInputFactory) {
		this.displayEventFactory = displayEventFactory;
		this.keyFactory = keyFactory;
		this.inputModifiersFactory = inputModifiersFactory;
		this.keyboardInputFactory = keyboardInputFactory;
		this.qType = qType;
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

		final Key key = this.keyFactory.createKey(keyCode);
		final InputModifiers inputModifiers = this.inputModifiersFactory
				.createInputModifiers(state);
		final KeyboardInput input = this.keyboardInputFactory
				.createKeyboardInputStarted(key, inputModifiers);

		return this.displayEventFactory.createKeyPressed(source, input);
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