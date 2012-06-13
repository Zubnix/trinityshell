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

import java.nio.ShortBuffer;

import org.trinity.display.x11.api.core.DataFormat;

/**
 * A <code>ShortDataContainer</code> is a helper class to read or write raw byte
 * data coming from a 16-bit <code>Short</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ShortDataContainer extends DataContainer<Short> {
	private ShortBuffer shortBuffer;

	/**
	 * Read existing data that is stored in a short (16 bits) format.
	 * 
	 * @param data
	 *            The data that will be read.
	 */
	public ShortDataContainer(final byte[] data) {
		super(DataFormat.SHORT, data);
		this.shortBuffer = getBuffer().asShortBuffer();
	}

	/**
	 * Write new data. The allocation is a factor of 2 bytes (16 bits).
	 * 
	 * @param nroDataBlocks
	 *            Number of 'shorts' (16 bits) to allocate.
	 */
	public ShortDataContainer(final int nroDataBlocks) {
		super(nroDataBlocks, DataFormat.SHORT);
	}

	@Override
	public void writeDataBlock(final Short dataBlock) {
		this.shortBuffer.put(dataBlock.shortValue());
	}

	@Override
	public Short readDataBlock() {
		return Short.valueOf(this.shortBuffer.get());
	}
}
