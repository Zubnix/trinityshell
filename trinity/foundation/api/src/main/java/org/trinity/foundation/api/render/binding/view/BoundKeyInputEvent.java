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
package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.InputSlotCaller;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/***************************************
 * A base implementation of a {@link BoundInputEvent} for {@link KeyboardInput}.
 * A bound button input event is both a {@link KeyNotifyEvent} and a
 * {@code BoundInputEvent}, so it can be used as an ordinary
 * {@link DisplayEvent} with additional information to invoke the correct
 * {@link InputSlot}.
 * 
 * @see InputSlotCaller
 *************************************** 
 */
public class BoundKeyInputEvent extends KeyNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	@AssistedInject
	BoundKeyInputEvent(	@Assisted final Object inputTarget,
						@Assisted final KeyboardInput input,
						@Assisted final String inputSlotName) {
		super(	inputTarget,
				input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}