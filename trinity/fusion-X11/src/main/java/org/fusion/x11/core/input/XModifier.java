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

import org.hydrogen.displayinterface.input.Keyboard.ModifierName;
import org.hydrogen.displayinterface.input.Modifier;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public enum XModifier implements Modifier {
	SHIFT_MODIFIER(1, ModifierName.MOD_SHIFT), LOCK_MODIFIER(2,
			ModifierName.MOD_LOCK), CTRL_MODIFIER(4, ModifierName.MOD_CTRL), MOD1_MODIFIER(
			8, ModifierName.MOD_1), MOD2_MODIFIER(16, ModifierName.MOD_2), MOD3_MODIFIER(
			32, ModifierName.MOD_3), MOD4_MODIFIER(64, ModifierName.MOD_4), MOD5_MODIFIER(
			128, ModifierName.MOD_5), ANY_MODIFIER(32768, ModifierName.MOD_ANY);

	private final int maskValue;
	private final ModifierName name;

	/**
	 * 
	 * @param maskValue
	 * @param name
	 */
	private XModifier(final int maskValue, final ModifierName name) {
		this.maskValue = maskValue;
		this.name = name;
	}

	@Override
	public int getModifierMask() {
		return this.maskValue;
	}

	@Override
	public ModifierName getModifierName() {
		return this.name;
	}
}
