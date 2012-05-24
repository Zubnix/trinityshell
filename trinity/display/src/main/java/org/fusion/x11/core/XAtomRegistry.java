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

package org.fusion.x11.core;

import java.util.HashMap;
import java.util.Map;

import org.trinity.core.display.api.DisplayAtoms;

// TODO documentation
/**
 * An <code>XAtomRegistry</code> groups all client side known {@link XAtom}s on
 * an given {@link XDisplay}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see Atom
 */
public class XAtomRegistry implements DisplayAtoms {
	private final Map<Long, XAtom> allXAtomsById;
	private final Map<String, XAtom> allXAtomsByName;

	/**
	 * 
	 * @param display
	 * 
	 */
	public XAtomRegistry(final XDisplay display) {
		this.allXAtomsById = new HashMap<Long, XAtom>();
		this.allXAtomsByName = new HashMap<String, XAtom>();
	}

	/**
	 * Register a <code>XAtom</code> that is already known on the X display
	 * server.
	 * 
	 * @param xAtom
	 *            A {@link XAtom}.
	 * @return the added {@link XAtom}.
	 */
	public <T extends XAtom> T register(final T xAtom) {
		if (this.allXAtomsById.containsKey(xAtom.getAtomId())
				|| this.allXAtomsById.containsValue(xAtom)) {
			throw new AssertionError("XAtom already registered!");
		}
		this.allXAtomsById.put(xAtom.getAtomId(), xAtom);
		this.allXAtomsByName.put(xAtom.getAtomName(), xAtom);
		return xAtom;
	}

	public XAtom getById(final Long id) {
		final XAtom xAtom = this.allXAtomsById.get(id);
		return xAtom;
	}

	@Override
	public XAtom getAtomByName(final String propertyName) {
		final XAtom atom = this.allXAtomsByName.get(propertyName);
		return atom;
	}

	@Override
	public XAtom[] getAtomsByNames(final String... atomNames) {
		final XAtom[] atoms = new XAtom[atomNames.length];
		for (int i = 0; i < atoms.length; i++) {
			atoms[i] = getAtomByName(atomNames[i]);
		}
		return atoms;
	}
}
