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
package org.trinity.display.x11.impl;

import org.trinity.display.x11.api.core.XAtom;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XAtomImpl implements XAtom {

	private final int atomId;
	private final String atomName;

	public XAtomImpl(final int atomId, final String atomName) {
		this.atomId = atomId;
		this.atomName = atomName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.display.api.property.Atom#getAtomName()
	 */
	@Override
	public String getName() {
		return this.atomName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.display.api.property.Atom#getAtomId()
	 */
	@Override
	public int getId() {
		return this.atomId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.atomId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XAtomImpl) {
			final XAtomImpl otherAtomImpl = (XAtomImpl) obj;
			return this.atomId == otherAtomImpl.getId();
		}
		return false;
	}
}
