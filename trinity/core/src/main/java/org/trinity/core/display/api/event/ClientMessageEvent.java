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

package org.trinity.core.display.api.event;

import org.trinity.core.display.api.Atom;

// TODO documentation
/**
 * A <code>ClientMessageEvent</code> contains data with information that is
 * meant for the receiver. This data is stored in a raw byte array. The type of
 * the message information is determined by the message type <code>Atom</code>.
 * The dataformat determines how the raw data should be read, as blocks of 8,
 * 16, 32 or 64 bits.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ClientMessageEvent extends DisplayEvent {

	/**
	 * 
	 * @return
	 */
	byte[] getData();

	/**
	 * 
	 * @return
	 */
	Atom getMessageType();

	/**
	 * 
	 * @return
	 */
	int getDataFormat();
}
