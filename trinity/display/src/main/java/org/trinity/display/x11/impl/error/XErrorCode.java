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
package org.trinity.display.x11.impl.error;

/**
 * An <code>XErrorCode</code> represents a X error. It wraps an X error code
 * with a short description.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XErrorCode {

	private final String description;
	final int            xCode;

	/**
	 * Create a new <code>XErrorCode</code> with the given X error code and the
	 * given description.
	 * 
	 * @param xCode
	 *            An X error code.
	 * @param description
	 *            The description for the X error code.
	 */
	XErrorCode(final int xCode,
	           final String description) {
		this.xCode = xCode;
		this.description = description;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final XErrorCode other = (XErrorCode) obj;
		if (this.xCode != other.xCode) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return this.xCode;
	}

	@Override
	public String toString() {
		return this.description;
	}

}
