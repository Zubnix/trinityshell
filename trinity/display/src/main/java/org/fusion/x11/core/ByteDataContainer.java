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

import org.trinity.display.x11.api.core.DataFormat;

/**
 * A <code>ByteDataContainer</code> is a helper class to read or write raw byte
 * data coming from a 8-bit <code>byte</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ByteDataContainer extends DataContainer<Byte> {

	/**
	 * Read existing data that is stored in 1 byte (8 bits) format.
	 * 
	 * @param data
	 *            The data that will be read.
	 */
	public ByteDataContainer(final byte[] data) {
		super(DataFormat.BYTE, data);
	}

	/**
	 * Write new data. The allocation is a factor of 1 byte (8 bits).
	 * 
	 * @param nroDataBlocks
	 *            Number of bytes (8 bits) to allocate.
	 */
	public ByteDataContainer(final int nroDataBlocks) {
		super(nroDataBlocks, DataFormat.BYTE);
	}

	@Override
	public void writeDataBlock(final Byte dataBlock) {
		this.getBuffer().put(dataBlock.byteValue());
	}

	@Override
	public Byte readDataBlock() {
		return Byte.valueOf(this.getBuffer().get());
	}
}
