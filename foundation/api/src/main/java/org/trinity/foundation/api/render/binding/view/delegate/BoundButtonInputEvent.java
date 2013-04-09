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

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.event.ButtonNotify;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.InputSlotCaller;

/***************************************
 * A base implementation of a {@link BoundInputEvent} for {@link PointerInput}.
 * A bound button input event is both a {@link ButtonNotify} and a
 * {@code BoundInputEvent}, so it can be used as an ordinary
 * {@link DisplayEvent} with additional information to invoke the correct
 * {@link InputSlot}.
 * 
 * @see InputSlotCaller
 *************************************** 
 */
@Immutable
public class BoundButtonInputEvent extends ButtonNotify implements BoundInputEvent {

	private final String inputSlotName;

	/**
	 * Create a new {@code BoundButtonInputEvent}. The given event target is
	 * usually the data context of the view that generated the input. The
	 * {@code PointerInput} is detail of the button input. The input slot name
	 * identifies the {@link InputSlot} of the event target.
	 * 
	 * @param displayEventTarget
	 *            an object with an {@link InputSlot}
	 * @param pointerInput
	 *            a {@link PointerInput}
	 * @param inputSlotName
	 *            an input slot name
	 */
	public BoundButtonInputEvent(	final PointerInput pointerInput,
									final String inputSlotName) {
		super(pointerInput);
		this.inputSlotName = inputSlotName;

	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}