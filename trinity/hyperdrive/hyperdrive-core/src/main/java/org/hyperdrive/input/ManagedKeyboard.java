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

import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.input.Keyboard;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.widget.RealRoot;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ManagedKeyboard extends GrabableInputDevice {
	private final Keyboard keyboard;

	/**
	 * 
	 * @param managedDisplay
	 */
	public ManagedKeyboard(final ManagedDisplay managedDisplay) {
		super(managedDisplay, KeyNotifyEvent.KEY_PRESSED,
				KeyNotifyEvent.KEY_RELEASED);
		this.keyboard = getManagedDisplay().getDisplay().getKeyBoard();
	}

	/**
	 * 
	 * @param keyNotifyEvent
	 * @return
	 */
	public String keyEventToString(final KeyNotifyEvent keyNotifyEvent) {
		String keyName;
		keyName = this.keyboard.keyName(keyNotifyEvent.getInput().getKey(),
				keyNotifyEvent.getInput().getModifiers());

		return keyName;
	}

	@Override
	protected void doEffectiveGrab() {
		RealRoot.get(getManagedDisplay()).getPlatformRenderArea()
				.catchAllKeyboardInput();
	}

	@Override
	protected void doEffectiveRelease() {
		RealRoot.get(getManagedDisplay()).getPlatformRenderArea()
				.stopKeyboardInputCatching();
	}
}