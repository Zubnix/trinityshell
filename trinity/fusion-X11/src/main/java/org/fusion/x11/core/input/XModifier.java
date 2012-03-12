/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.input;

import org.hydrogen.api.display.input.InputModifierName;
import org.hydrogen.api.display.input.Modifier;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public enum XModifier implements Modifier {
	SHIFT_MODIFIER(1, InputModifierName.MOD_SHIFT), LOCK_MODIFIER(2,
			InputModifierName.MOD_LOCK), CTRL_MODIFIER(4, InputModifierName.MOD_CTRL), MOD1_MODIFIER(
			8, InputModifierName.MOD_1), MOD2_MODIFIER(16, InputModifierName.MOD_2), MOD3_MODIFIER(
			32, InputModifierName.MOD_3), MOD4_MODIFIER(64, InputModifierName.MOD_4), MOD5_MODIFIER(
			128, InputModifierName.MOD_5), ANY_MODIFIER(32768, InputModifierName.MOD_ANY);

	private final int maskValue;
	private final InputModifierName name;

	/**
	 * 
	 * @param maskValue
	 * @param name
	 */
	private XModifier(final int maskValue, final InputModifierName name) {
		this.maskValue = maskValue;
		this.name = name;
	}

	@Override
	public int getModifierMask() {
		return this.maskValue;
	}

	@Override
	public InputModifierName getModifierName() {
		return this.name;
	}
}
