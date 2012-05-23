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
package org.trinity.core.display.impl;

import org.trinity.core.display.api.Atom;

import com.google.inject.Inject;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class PropertyInstanceText extends BasePropertyInstance {

	private final String text;

	// public PropertyInstanceText(final Display display,
	// final String encodingTypeName,
	// final String text) {
	// super(display,
	// encodingTypeName);
	// this.text = text;
	// }

	/**
	 * 
	 * @param encodigType
	 * @param text
	 */
	@Inject
	public PropertyInstanceText(final Atom encodigType, final String text) {
		super(encodigType);
		this.text = text;

	}

	/**
	 * 
	 * @return
	 */
	public String getText() {
		return this.text;
	}
}
