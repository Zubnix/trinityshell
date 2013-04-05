package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.view.delegate.BoundButtonInputEvent;
import org.trinity.foundation.api.render.binding.view.delegate.BoundKeyInputEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.OwnerThread;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;

@NotThreadSafe
@OwnerThread("Render")
public class BoundInputListener extends QObject {

	private final Class<? extends Input> inputType;
	private final AsyncListenable inputEventTarget;
	private final String inputSlotName;

	BoundInputListener(	final Class<? extends Input> inputType,
						final AsyncListenable inputEventTarget,
						final String inputSlotName) {
		this.inputType = inputType;
		this.inputEventTarget = inputEventTarget;
		this.inputSlotName = inputSlotName;
	}

	@Override
	public boolean eventFilter(	final QObject thisObj,
								final QEvent qEvent) {
		final Type eventType = qEvent.type();

		if ((eventType == Type.KeyPress) && KeyboardInput.class.isAssignableFrom(this.inputType.getClass())) {
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

			final BoundKeyInputEvent boundKeyInputEvent = new BoundKeyInputEvent(	keyboardInput,
																					this.inputSlotName);
			this.inputEventTarget.post(boundKeyInputEvent);
			return false;
		}

		if ((eventType == Type.KeyRelease) && KeyboardInput.class.isAssignableFrom(this.inputType.getClass())) {
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

			final BoundKeyInputEvent boundKeyInputEvent = new BoundKeyInputEvent(	keyboardInput,
																					this.inputSlotName);
			this.inputEventTarget.post(boundKeyInputEvent);
			return false;
		}

		if ((eventType == Type.MouseButtonPress) && PointerInput.class.isAssignableFrom(this.inputType.getClass())) {
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
			final BoundButtonInputEvent buttonInputEvent = new BoundButtonInputEvent(	pointerInput,
																						this.inputSlotName);
			this.inputEventTarget.post(buttonInputEvent);
			return false;
		}

		if ((eventType == Type.MouseButtonRelease) && PointerInput.class.isAssignableFrom(this.inputType.getClass())) {
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
			final BoundButtonInputEvent buttonInputEvent = new BoundButtonInputEvent(	pointerInput,
																						this.inputSlotName);
			this.inputEventTarget.post(buttonInputEvent);
			return false;
		}

		return false;
	}
}