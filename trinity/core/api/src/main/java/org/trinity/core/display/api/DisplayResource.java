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
package org.trinity.core.display.api;

/**
 * A <code>DisplayResource</code> is a unique resource living on a
 * <code>Display</code>. Examples of a <code>DisplayResource</code> can be a
 * native window, a native buffer, ...
 * <p>
 * A <code>DisplayResource</code> can be identified by it's
 * <code>DisplayResourceHandle</code>, which defines both the
 * <code>Display</code> and the resource handle of the
 * <code>DisplayResource</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface DisplayResource {

	/**
	 * A unique identifier for this <code>DisplayResource</code>.
	 * 
	 * @return a unique {@link DisplayResourceHandle}
	 */
	DisplayResourceHandle getDisplayResourceHandle();

}
