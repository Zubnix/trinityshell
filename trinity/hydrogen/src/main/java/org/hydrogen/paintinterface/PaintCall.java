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
package org.hydrogen.paintinterface;

/**
 * A <code>PaintCall</code> wraps calls to a paint toolkit and as such is
 * executed by the paint back-end thread.It implements a paint back-end specific
 * call. A <code>PaintCall</code> is not necessarily executed by the thread that
 * created it but rather by the back-end paint thread. It is thus a good
 * practice that the <code>PaintCall</code> does not make any state manipulating
 * calls to objects that don't "live" in the back-end paint thread else
 * concurrency errors might be introduced.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * 
 * @param <P>
 *            The paint peer type as used by the paint toolkit.
 * @param <R>
 *            An optional return type.
 */
public interface PaintCall<P, R> {
	/**
	 * Implements a custom paint back-end operation for the provided
	 * <code>Paintable</code>. This method will be called by the paint back-end
	 * thread.
	 * 
	 * @param paintPeer
	 *            Gives access to the underlying paint back-end through a paint
	 *            toolkit specific object that represents the given
	 *            <code>Paintable</code>.
	 * @param paintable
	 *            The {@link Paintable} on who's behalve the paint call takes
	 *            place.
	 * 
	 * @return The return object depends on the paint back-end implementation.
	 *         This can be a modified or entirely new paint peer in case the
	 *         given paintpeer needs to be replaced. Mostly however it should
	 *         return null.
	 */
	R call(P paintPeer, Paintable paintable);
}
