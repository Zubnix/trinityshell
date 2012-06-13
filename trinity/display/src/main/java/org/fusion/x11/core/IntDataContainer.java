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

import java.nio.IntBuffer;

import org.trinity.display.x11.api.core.DataFormat;

/**
 * An <code>IntDataContainer</code> is a helper class to read or write raw byte
 * data coming from a 32-bit <code>Integer</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class IntDataContainer extends DataContainer<Integer> {
	private final IntBuffer intBuffer;

	/**
	 * Read existing data that is stored in a 4 bytes (32 bits) format.
	 * 
	 * @param data
	 *            The data that will be read.
	 */
	public IntDataContainer(final byte[] data) {
		super(DataFormat.INTEGER, data);
		this.intBuffer = getBuffer().asIntBuffer();
	}

	/**
	 * Write new data. The allocation is a factor of 4 bytes (32 bits).
	 * 
	 * @param nroInts
	 *            Number of 4 bytes (32 bits) to allocate.
	 */
	public IntDataContainer(final int nroInts) {
		this(new byte[(nroInts * DataFormat.INTEGER.getFormat()) / 8]);
	}

	@Override
	public void writeDataBlock(final Integer dataBlock) {
		this.intBuffer.put(dataBlock.intValue());
	}

	@Override
	public Integer readDataBlock() {
		return Integer.valueOf(this.intBuffer.get());
	}
}
