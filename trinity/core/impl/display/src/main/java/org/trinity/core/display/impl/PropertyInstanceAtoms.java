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

import java.util.Arrays;

import org.trinity.core.display.api.Atom;

import com.google.inject.Inject;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class PropertyInstanceAtoms extends BasePropertyInstance {

	private final Atom[] atoms;

	// public PropertyInstanceAtoms(final Display display, final String
	// typeName,
	// final Atom... atoms) {
	// super(display, typeName);
	// this.atoms = atoms;
	// }

	/**
	 * 
	 * @param type
	 * @param atoms
	 */
	@Inject
	protected PropertyInstanceAtoms(final Atom type, final Atom... atoms) {
		super(type);
		this.atoms = atoms;
	}

	/**
	 * 
	 * @return
	 */
	public Atom[] getAtoms() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.atoms, this.atoms.length);
	}
}
