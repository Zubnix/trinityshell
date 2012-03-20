/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.input;

import org.hydrogen.api.display.event.KeyNotifyEvent;
import org.hydrogen.api.display.input.Keyboard;
import org.hydrogen.api.display.input.KeyboardInput;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.event.KeyboardKeyPressedHandler;
import org.hyperdrive.api.core.event.KeyboardKeyReleasedHandler;
import org.hyperdrive.api.input.ManagedKeyboard;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseManagedKeyboard extends BaseInputDevice implements
		ManagedKeyboard {

	private final class KeyPressedForwarder extends KeyboardKeyPressedHandler {
		@Override
		public void handleEvent(final KeyNotifyEvent event) {
			delegateInputEventToInputEventManagers(event);
		}
	}

	private final class KeyReleasedForwarder extends KeyboardKeyReleasedHandler {
		@Override
		public void handleEvent(final KeyNotifyEvent event) {
			delegateInputEventToInputEventManagers(event);
		}
	}

	private final Keyboard keyboard;
	private final KeyboardKeyPressedHandler keyPressedForwarder;
	private final KeyboardKeyReleasedHandler keyReleasedForwarder;

	/**
	 * 
	 * @param managedDisplay
	 */
	public BaseManagedKeyboard(final ManagedDisplay managedDisplay) {
		super(managedDisplay);
		this.keyboard = getManagedDisplay().getDisplay().getKeyBoard();
		this.keyPressedForwarder = new KeyPressedForwarder();
		this.keyReleasedForwarder = new KeyReleasedForwarder();
	}

	/**
	 * 
	 * @param keyNotifyEvent
	 * @return
	 */
	@Override
	public String keyEventToString(final KeyNotifyEvent keyNotifyEvent) {

		final KeyboardInput keyboardInput = keyNotifyEvent.getInput();
		final String keyName = this.keyboard.keyName(keyboardInput.getKey(),
				keyboardInput.getModifiers());

		return keyName;
	}

	@Override
	public void release() {
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.stopKeyboardInputCatching();
		getManagedDisplay().removeDisplayEventHandler(this.keyPressedForwarder);
		getManagedDisplay()
				.removeDisplayEventHandler(this.keyReleasedForwarder);
	}

	@Override
	protected void delegateInputEventsAndGrab() {
		getManagedDisplay().getRoot().getPlatformRenderArea()
				.catchAllKeyboardInput();
		getManagedDisplay().addDisplayEventHandler(this.keyPressedForwarder);
		getManagedDisplay().addDisplayEventHandler(this.keyReleasedForwarder);
	}

}