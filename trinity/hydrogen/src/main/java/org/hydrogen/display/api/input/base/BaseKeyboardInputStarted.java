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
package org.hydrogen.display.api.input.base;

import org.hydrogen.display.api.input.InputModifiers;
import org.hydrogen.display.api.input.Key;
import org.hydrogen.display.api.input.Momentum;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class BaseKeyboardInputStarted extends BaseKeyboardInput {

	/*****************************************
	 * @param momentum
	 * @param key
	 * @param inputModifiers
	 ****************************************/
	@Inject
	protected BaseKeyboardInputStarted(	@Named("Started") final Momentum momentum,
										@Assisted final Key key,
										@Assisted final InputModifiers inputModifiers) {
		super(momentum, key, inputModifiers);
	}

}
