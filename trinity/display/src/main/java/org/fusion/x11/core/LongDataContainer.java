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

import java.nio.LongBuffer;

/**
 * A <code>LongDataContainer</code> is a helper class to read or write raw byte
 * data coming from a 64-bit <code>Long</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class LongDataContainer extends DataContainer<Long> {

	private final LongBuffer longBuffer;

	/**
	 * Read existing data that is stored in a 8 bytes (64 bits) format.
	 * 
	 * @param data
	 *            The data that will be read.
	 */
	public LongDataContainer(byte[] data) {
		super(DataFormat.LONG, data);
		longBuffer = getBuffer().asLongBuffer();
	}

	/**
	 * Write new data. The allocation is a factor of 8 bytes (64 bits).
	 * 
	 * @param nroLongs
	 *            Number of 'longs' (64 bits) to allocate.
	 */
	public LongDataContainer(int nroLongs) {
		this(new byte[(nroLongs * DataFormat.LONG.getFormat()) / 8]);
	}

	@Override
	public void writeDataBlock(Long dataBlock) {
		longBuffer.put(dataBlock.longValue());
	}

	@Override
	public Long readDataBlock() {
		return Long.valueOf(longBuffer.get());
	}
}
