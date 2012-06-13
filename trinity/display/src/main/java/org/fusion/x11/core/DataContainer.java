/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.fusion.x11.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.trinity.display.x11.api.core.DataFormat;

/**
 * A <code>DataContainer</code> is an abstract helper class to convert a java
 * type to a byte array.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class DataContainer<T> {

	private final ByteBuffer byteBuffer;
	private final DataFormat format;

	/**
	 * Read existing data.
	 * 
	 * @param format
	 * @param data
	 */
	public DataContainer(final DataFormat format, final byte[] data) {
		this(format, ByteBuffer.wrap(data));
	}

	/**
	 * Read existing data.
	 * 
	 * @param format
	 * @param buffer
	 */
	protected DataContainer(final DataFormat format, final ByteBuffer buffer) {
		// TODO improve performance by allocating a static reusable threadlocal
		// bytebuffer?
		this.byteBuffer = buffer.order(ByteOrder.nativeOrder());
		this.format = format;
	}

	/**
	 * Write new data.
	 * 
	 * @param nroDataBlocks
	 * @param format
	 */
	public DataContainer(final int nroDataBlocks, final DataFormat format) {
		this(format, ByteBuffer.allocate(nroDataBlocks
				* (format.getFormat() / 4)));
	}

	/**
	 * 
	 * @return
	 */
	public ByteBuffer getBuffer() {
		return this.byteBuffer;
	}

	/**
	 * 
	 * @param rawData
	 */
	public void writeRawData(final byte[] rawData) {
		this.byteBuffer.put(rawData);
	}

	/**
	 * 
	 * @param dataBlock
	 */
	public abstract void writeDataBlock(T dataBlock);

	/**
	 * 
	 * @return
	 */
	public abstract T readDataBlock();

	/**
	 * 
	 * @return
	 */
	public DataFormat getDataFormat() {
		return this.format;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getAllData() {
		if (this.byteBuffer.hasArray()) {
			return this.byteBuffer.array();
		} else {
			throw new RuntimeException("DataContainer has no backing array.");
		}
	}

	// /**
	// *
	// * @param length
	// * @return
	// */
	// public byte[] getData(final int length) {
	// if (this.byteBuffer.hasArray()) {
	// final byte[] returnBytes = new byte[length];
	// this.byteBuffer.get(returnBytes);
	// return returnBytes;
	// } else {
	// throw new RuntimeException("DataContainer has no backing array.");
	// }
	// }

	// /**
	// *
	// * @param offset
	// * @param length
	// * @return
	// */
	// public byte[] getData(final int offset, final int length) {
	// if (this.byteBuffer.hasArray()) {
	// final byte[] returnBytes = new byte[length];
	// this.byteBuffer.get(returnBytes, offset, length);
	// return returnBytes;
	// } else {
	// throw new RuntimeException("DataContainer has no backing array.");
	// }
	// }
}
