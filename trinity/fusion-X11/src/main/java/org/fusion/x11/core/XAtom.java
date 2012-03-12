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

import org.hydrogen.api.display.Atom;

/**
 * An <code>Atom</code> linked to an <code>XDisplay</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see Atom
 */
public class XAtom implements Atom {

	private final String atomName;
	private final Long atomId;
	private final XDisplay display;

	/**
	 * Construct an <code>Atom</code> that is already known by the given
	 * <code>XDisplay</code>. This will not attempt to allocate a new atom on
	 * the native display, but functions as a client side mapping of an already
	 * known atom.
	 * <p>
	 * IMPORTANT:
	 * <p>
	 * It is recommended to use the <code>XAtomRegistry</code> when retrieving
	 * an <code>XAtom</code> instead of creating a new <code>XAtom</code>, as
	 * this will cache the <code>XAtom</code> and make it available to other
	 * objects querying the <code>XAtom</code>.
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 * @see XAtomRegistry#getAtomByName(String)
	 * @see XAtomRegistry#getById(Long)
	 */
	public XAtom(final XDisplay display, final String atomName,
			final Long atomId) {
		this.display = display;
		this.atomName = atomName;
		this.atomId = atomId;
	}

	/**
	 * Construct a new <code>Atom</code> that is not known on the given
	 * <code>XDisplay</code>. This will result in a call to the X display server
	 * to allocate a new atom.
	 * <p>
	 * IMPORTANT:
	 * <p>
	 * It is recommended to use the <code>XAtomRegistry</code> when retrieving
	 * an <code>XAtom</code> instead of creating a new <code>XAtom</code>, as
	 * this will cache the <code>XAtom</code> and make it available to other
	 * objects querying the <code>XAtom</code>.
	 * 
	 * @param display
	 * @param atomName
	 * @  
	 * @see XAtomRegistry#getAtomByName(String)
	 */
	public XAtom(final XDisplay display, final String atomName) {
		this(display, atomName, display.getXCoreInterface().internAtom(display,
				atomName));
	}

	@Override
	public Long getAtomId() {
		return this.atomId;
	}

	@Override
	public String getAtomName() {
		return this.atomName;
	}

	@Override
	public XDisplay getDisplay() {
		return this.display;
	}

}
