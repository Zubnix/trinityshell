/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface.input;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class InputModifiers {

	private int inputModifersMask;

	/**
	 * 
	 */
	public InputModifiers() {
		this.inputModifersMask = 0;
	}

	/**
	 * 
	 * @param inputModifersMask
	 */
	public InputModifiers(final int inputModifersMask) {
		this.inputModifersMask = inputModifersMask;
	}

	/**
	 * 
	 * @param modifiers
	 */
	public InputModifiers(final Modifier... modifiers) {
		this();
		this.setModifiers(modifiers);
	}

	/**
	 * 
	 * @param modifier
	 * @return
	 */
	public boolean isModifierSet(final Modifier modifier) {
		return (this.inputModifersMask & modifier.getModifierMask()) != 0;
	}

	/**
	 * 
	 * @param modifiers
	 */
	public void setModifiers(final Modifier... modifiers) {
		for (final Modifier modifier : modifiers) {
			this.inputModifersMask |= modifier.getModifierMask();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getInputModifiersMask() {
		return this.inputModifersMask;
	}
}
