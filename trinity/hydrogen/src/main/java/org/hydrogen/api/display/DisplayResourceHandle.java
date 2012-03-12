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
package org.hydrogen.api.display;

/**
 * A <code>DisplayResourceHandle</code> is a unique identifier for a
 * {@link DisplayResource}. It forms the relation for both a
 * <code>Display</code> as well as a <code>ResourceHandle</code> to create a
 * unique <code>DisplayResourceHandle</code>.
 * <p>
 * Implementation advice:
 * <p>
 * A <code>DisplayResourceHandle</code> instance should be unique so that for
 * every <code>DisplayResourceHandle</code> A and every
 * <code>DisplayResourceHandle</code> B where A.equals(B) is true, A == B should
 * also be true.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface DisplayResourceHandle {

	/**
	 * The <code>Display</code> that created this
	 * <code>DisplayResourceHandle</code>.
	 * 
	 * @return A <code>Display</code>.
	 */
	Display getDisplay();

	/**
	 * The <code>ResourceHandle</code> is a unique identifier and is bound and
	 * limited to the <code>Display</code> that it defined.
	 * 
	 * @return A unique resource handle.
	 */
	ResourceHandle getResourceHandle();
}
