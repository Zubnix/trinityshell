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

package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;
import org.trinity.foundation.api.render.bindkey.RenderExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;

@NotThreadSafe
@ExecutionContext(RenderExecutor.class)
public class BoundInputListener extends QObject {

	private final Class<? extends Input> inputType;
	private final InputSlotCallerDelegate inputSlotCallerDelegate;
	private final Object inputEventTarget;
	private final String inputSlotName;

	BoundInputListener(	final Class<? extends Input> inputType,
						final InputSlotCallerDelegate inputSlotCallerDelegate,
						final Object inputEventTarget,
						final String inputSlotName) {
		this.inputType = inputType;
		this.inputSlotCallerDelegate = inputSlotCallerDelegate;
		this.inputEventTarget = inputEventTarget;
		this.inputSlotName = inputSlotName;
	}

	@Override
	public boolean eventFilter(	final QObject thisObj,
								final QEvent qEvent) {
		final Type eventType = qEvent.type();

		if ((eventType == Type.KeyPress) && KeyboardInput.class.isAssignableFrom(this.inputType)) {
			qEvent.accept();
			final QKeyEvent keyEvent = (QKeyEvent) qEvent;

			if (keyEvent.isAutoRepeat()) {
				return false;
			}

			final int keyCode = keyEvent.nativeScanCode();

			final int state = keyEvent.nativeModifiers();

			final Key key = new Key(keyCode);
			final InputModifiers inputModifiers = new InputModifiers(state);
			final KeyboardInput keyboardInput = new KeyboardInput(	Momentum.STARTED,
																	key,
																	inputModifiers);

			this.inputSlotCallerDelegate.callInputSlot(	this.inputEventTarget,
														this.inputSlotName,
														keyboardInput);
			return true;
		}

		if ((eventType == Type.KeyRelease) && KeyboardInput.class.isAssignableFrom(this.inputType)) {
			qEvent.accept();
			final QKeyEvent keyEvent = (QKeyEvent) qEvent;

			if (keyEvent.isAutoRepeat()) {
				return false;
			}

			final int keyCode = keyEvent.nativeScanCode();

			final int state = keyEvent.nativeModifiers();

			final Key key = new Key(keyCode);
			final InputModifiers inputModifiers = new InputModifiers(state);
			final KeyboardInput keyboardInput = new KeyboardInput(	Momentum.STOPPED,
																	key,
																	inputModifiers);

			this.inputSlotCallerDelegate.callInputSlot(	this.inputEventTarget,
														this.inputSlotName,
														keyboardInput);
			return true;
		}

		if ((eventType == Type.MouseButtonPress) && PointerInput.class.isAssignableFrom(this.inputType)) {
			qEvent.accept();

			final QMouseEvent qMouseEvent = (QMouseEvent) qEvent;

			final int buttonCode = qMouseEvent.button().value();
			final int state = qMouseEvent.modifiers().value();

			final int rootX = qMouseEvent.globalX();
			final int rootY = qMouseEvent.globalY();
			final int relativeX = qMouseEvent.x();
			final int relativeY = qMouseEvent.y();

			final Button button = new Button(buttonCode);
			final InputModifiers inputModifiers = new InputModifiers(state);
			final PointerInput pointerInput = new PointerInput(	Momentum.STARTED,
																button,
																inputModifiers,
																relativeX,
																relativeY,
																rootX,
																rootY);

			this.inputSlotCallerDelegate.callInputSlot(	this.inputEventTarget,
														this.inputSlotName,
														pointerInput);
			return true;
		}

		if ((eventType == Type.MouseButtonRelease) && PointerInput.class.isAssignableFrom(this.inputType)) {
			qEvent.accept();

			final QMouseEvent qMouseEvent = (QMouseEvent) qEvent;

			final int buttonCode = qMouseEvent.button().value();
			final int state = qMouseEvent.modifiers().value();

			final int rootX = qMouseEvent.globalX();
			final int rootY = qMouseEvent.globalY();
			final int relativeX = qMouseEvent.x();
			final int relativeY = qMouseEvent.y();

			final Button button = new Button(buttonCode);
			final InputModifiers inputModifiers = new InputModifiers(state);
			final PointerInput pointerInput = new PointerInput(	Momentum.STOPPED,
																button,
																inputModifiers,
																relativeX,
																relativeY,
																rootX,
																rootY);
			this.inputSlotCallerDelegate.callInputSlot(	this.inputEventTarget,
														this.inputSlotName,
														pointerInput);
			return true;
		}

		return false;
	}
}
