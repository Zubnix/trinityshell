/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.input.impl;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.foundation.api.event.KeyboardKeyPressedHandler;
import org.trinity.shell.foundation.api.event.KeyboardKeyReleasedHandler;
import org.trinity.shell.input.api.ManagedKeyboard;
import org.trinity.shell.widget.api.Root;

import com.google.inject.Inject;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ManagedKeyboardImpl extends AbstractInputDevice implements
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

	private final Root root;
	private final ManagedDisplay managedDisplay;

	private final Keyboard keyboard;

	private final KeyboardKeyPressedHandler keyPressedForwarder;
	private final KeyboardKeyReleasedHandler keyReleasedForwarder;

	/**
	 * @param managedDisplay
	 */
	@Inject
	protected ManagedKeyboardImpl(	final ManagedDisplay managedDisplay,
									final Root root,
									final Keyboard keyboard) {
		this.managedDisplay = managedDisplay;
		this.root = root;
		this.keyboard = keyboard;
		this.keyPressedForwarder = new KeyPressedForwarder();
		this.keyReleasedForwarder = new KeyReleasedForwarder();
	}

	/**
	 * @param keyNotifyEvent
	 * @return
	 */
	@Override
	public String keyEventToString(final KeyNotifyEvent keyNotifyEvent) {

		final KeyboardInput keyboardInput = keyNotifyEvent.getInput();
		final String keyName = this.keyboard
				.keyName(keyboardInput.getKey(), keyboardInput.getModifiers());

		return keyName;
	}

	@Override
	public void release() {
		this.root.getPlatformRenderArea().stopKeyboardInputCatching();
		this.managedDisplay.removeDisplayEventHandler(this.keyPressedForwarder);
		this.managedDisplay
				.removeDisplayEventHandler(this.keyReleasedForwarder);
	}

	@Override
	protected void delegateInputEventsAndGrab() {
		this.root.getPlatformRenderArea().catchAllKeyboardInput();
		this.managedDisplay.addDisplayEventHandler(this.keyPressedForwarder);
		this.managedDisplay.addDisplayEventHandler(this.keyReleasedForwarder);
	}

}