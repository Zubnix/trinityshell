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
package org.hydrogen.displayinterface;

import java.util.Arrays;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class PropertyInstanceNumbers extends BasePropertyInstance {

	private final int[] numbers;

	/**
	 * 
	 * @param display
	 * @param typeName
	 * @param numbers
	 */
	public PropertyInstanceNumbers(final Display display,
			final String typeName, final int... numbers) {
		super(display, typeName);
		this.numbers = numbers;
	}

	/**
	 * 
	 * @param encodingType
	 * @param number
	 */
	public PropertyInstanceNumbers(final Atom encodingType,
			final int... numbers) {
		super(encodingType);
		this.numbers = numbers;
	}

	/**
	 * 
	 * @return
	 */
	public int[] getNumbers() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.numbers, this.numbers.length);
	}
}