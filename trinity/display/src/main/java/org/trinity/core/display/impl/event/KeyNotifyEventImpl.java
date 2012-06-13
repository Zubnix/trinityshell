/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.core.display.impl.event;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.DisplayEventType;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.KeyboardInput;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * A <code>BaseKeyNotifyEvent</code> is a basic implementation of a
 * <code>KeyNotifyEvent</code>. Classes wishing to implement
 * <code>KeyNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class KeyNotifyEventImpl extends InputNotifyEventImpl<KeyboardInput>
		implements KeyNotifyEvent {

	/**
	 * @param eventType
	 * @param eventSource
	 * @param input
	 */
	@Inject
	protected KeyNotifyEventImpl(	final DisplayEventType eventType,
									@Assisted final DisplayEventSource eventSource,
									@Assisted final KeyboardInput input) {
		super(eventType, eventSource, input);
	}

	@Override
	public String toString() {
		return String.format("%s, keycode: %d, modifiersmask: %d", super
				.toString(), getInput().getKey().getKeyCode(), getInput()
				.getModifiers().getInputModifiersMask());
	}
}
