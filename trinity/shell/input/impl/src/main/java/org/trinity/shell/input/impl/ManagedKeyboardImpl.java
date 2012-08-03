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

import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.event.KeyNotifyEvent;
import org.trinity.shell.input.api.ManagedKeyboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class ManagedKeyboardImpl extends AbstractInputDevice implements
		ManagedKeyboard {

	private final Keyboard keyboard;

	@Inject
	protected ManagedKeyboardImpl(final Keyboard keyboard) {

		this.keyboard = keyboard;
	}

	@Override
	public String keyEventToString(final KeyNotifyEvent keyNotifyEvent) {

		final KeyboardInput keyboardInput = keyNotifyEvent.getInput();
		final String keyName = this.keyboard
				.keyName(keyboardInput.getKey(), keyboardInput.getModifiers());

		return keyName;
	}

	@Override
	public void release() {
		this.keyboard.stopKeyboardInputCatching();
	}

	@Override
	protected void delegateInputEventsAndGrab() {
		this.keyboard.catchAllKeyboardInput();
	}
}