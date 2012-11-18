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
package org.trinity.foundation.input.api;

/***************************************
 * Identifies a pointing device's button ie. a mouse button.
 * 
 *************************************** 
 */
public class Button {

	private final int buttonCode;

	/***************************************
	 * Construct a new <code>Button</code> with the given native button code.
	 * 
	 * @param buttonCode
	 *            The native code of this <code>Button</code>.
	 *************************************** 
	 */
	public Button(final int buttonCode) {
		this.buttonCode = buttonCode;
	}

	/****************************************
	 * The native code of this <code>Button</code>. This code uniquely
	 * identifies a <code>Button</code>.
	 * 
	 * @return The native code of this <code>Button</code>.
	 *************************************** 
	 */
	public int getButtonCode() {
		return this.buttonCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Button) {
			final Button otherButton = (Button) obj;
			return otherButton.getButtonCode() == getButtonCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getButtonCode();
	}
}
