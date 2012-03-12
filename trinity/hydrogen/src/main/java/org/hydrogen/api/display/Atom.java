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
 * An <code>Atom</code> is a name ( <code>String</code> ) mapped to a number (
 * <code>Integer</code>). The concept of the <code>Atom</code> class is derived
 * from the same concept of an atom as used on the X display server or the
 * windows gdi interface. Once defined on a <code>Display</code>, an
 * <code>Atom</code> is final in both number and name and consistent for all
 * programs communicating with that <code>Display</code>.
 * <p>
 * An <code>Atom</code> is mostly used when assigning properties a
 * {@link PlatformRenderArea} or when sending a {@link ClientMessageEvent}. The
 * <code>Atom</code> is used as both an identifier of raw byte data, as well as
 * being raw data itself.
 * <p>
 * For example say we have an array of integers in a
 * <code>ClientMessageEvent</code>. In itself this array has no meaning. However
 * when we say the array contents is of <code>Atom</code> "WINDOW" ("WINDOW"
 * being the <code>Atom</code> name) then we know the integers in the array are
 * actually window ids. The underlying system assigns this atom by assigning the
 * number associated with the "WINDOW" atom to the atom type of the array in the
 * <code>ClientMessageEvent</code>.
 * <p>
 * Another example is the use of <code>Atom</code>s when using a property on a
 * <code>PlatformRenderArea</code>. A property is defined by the property name
 * and the type of data the property contains. This means one <code>Atom</code>
 * for the property name and one <code>Atom</code> to identify the contents of
 * the property. This is reflected by {@link Property} which extends from
 * <code>Atom</code> and {@link PropertyInstance}, which returns an
 * <code>Atom</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Atom {

	/**
	 * The unique name of the <code>Atom</code>.
	 * 
	 * @return
	 */
	String getAtomName();

	/**
	 * The unique id of the <code>Atom</code>.
	 * 
	 * @return
	 */
	Long getAtomId();

	/**
	 * The <code>Display</code> on which this <code>Atom</code> is bound to.
	 * 
	 * @return
	 */
	Display getDisplay();
}
