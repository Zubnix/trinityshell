/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.render.binding.view.delegate;

import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.InputSlotCaller;

/***************************************
 * A base implementation of a {@link BoundInputEvent} for {@link KeyboardInput}.
 * A bound button input event is both a {@link KeyNotify} and a
 * {@code BoundInputEvent}, so it can be used as an ordinary
 * {@link DisplayEvent} with additional information to invoke the correct
 * {@link InputSlot}.
 * 
 * @see InputSlotCaller
 *************************************** 
 */
public class BoundKeyInputEvent extends KeyNotify implements BoundInputEvent {

	private final String inputSlotName;

	/**
	 * Create a new {@code BoundKeyInputEvent}. The given event target is
	 * usually the data context of the view that generated the input. The
	 * {@code KeyboardInput} is detail of the key input. The input slot name
	 * identifies the {@link InputSlot} of the event target.
	 * 
	 * @param inputTarget
	 *            an object with an {@link InputSlot}.
	 * @param input
	 *            a {@link KeyboardInput}
	 * @param inputSlotName
	 *            an input slot name.
	 */
	public BoundKeyInputEvent(	final KeyboardInput input,
								final String inputSlotName) {
		super(input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}