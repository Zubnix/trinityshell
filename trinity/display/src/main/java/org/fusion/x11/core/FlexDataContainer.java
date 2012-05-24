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

import java.nio.ByteBuffer;

// TODO documentation
/**
 * A <code>FlexDataContainer</code> is a helper class to read or write raw byte
 * date coming from a multiple <code>Number</code>s with a variable bit size.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class FlexDataContainer extends DataContainer<Number> {

	public static final int DEFAULT_BYTE_SIZE = 1;
	public static final int DEFAULT_SHORT_SIZE = 2;
	public static final int DEFAULT_INT_SIZE = 4;
	public static final int DEFAULT_LONG_SIZE = 8;

	private int byteSize = DEFAULT_BYTE_SIZE;
	private int shortSize = DEFAULT_SHORT_SIZE;
	private int intSize = DEFAULT_INT_SIZE;
	private int longSize = DEFAULT_LONG_SIZE;

	/**
	 * Write new data. The allocation is a factor of 1 byte (8 bits).
	 * 
	 * @param nroBytes
	 */
	public FlexDataContainer(final int nroBytes) {
		super(nroBytes, DataFormat.BYTE);
	}

	/**
	 * Read or write data stored in the given <code>ByteBuffer</code>.
	 * 
	 * @param buffer
	 */
	public FlexDataContainer(final ByteBuffer buffer) {
		super(DataFormat.BYTE, buffer);
	}

	/**
	 * Write a number as an array of bytes. The number of bytes is determined by
	 * the type of the given <code>Number</code>.
	 */
	@Override
	public void writeDataBlock(final Number dataBlock) {
		if (dataBlock instanceof Byte) {
			this.getBuffer().put(dataBlock.byteValue());
		} else if (dataBlock instanceof Short) {
			this.getBuffer().putShort(dataBlock.shortValue());
		} else if (dataBlock instanceof Integer) {
			this.getBuffer().putInt(dataBlock.intValue());
		} else if (dataBlock instanceof Long) {
			this.getBuffer().putLong(dataBlock.longValue());
		} else {
			throw new Error("Unknown number type! Got type: "
					+ dataBlock.getClass());
		}
	}

	// TODO make this method generic and determine the number of bytes to be
	// read by the generic type.
	/**
	 * Read a byte
	 */
	@Override
	public Number readDataBlock() {
		return Integer.valueOf(this.readSignedByte());
	}

	/**
	 * The number of bits in a byte. Default is 8.
	 * 
	 * @return
	 */
	public int getByteSize() {
		return this.byteSize;
	}

	/**
	 * The number of bits in a short. Default is 16.
	 * 
	 * @return
	 */
	public int getShortSize() {
		return this.shortSize;
	}

	/**
	 * The number of bits in a long. Default is 64.
	 * 
	 * @return
	 */
	public int getLongSize() {
		return this.longSize;
	}

	/**
	 * The number of bits in an int. Default is 32.
	 * 
	 * @return
	 */
	public int getIntSize() {
		return this.intSize;
	}

	/**
	 * Change the number of bits mapped to a byte. Default this is set to 8.
	 * 
	 * @param byteSize
	 */
	public void setByteSize(final int byteSize) {
		this.byteSize = byteSize;
	}

	/**
	 * Change the number of bits mapped to a byte. Default this is set to 32.
	 * 
	 * @param intSize
	 */
	public void setIntSize(final int intSize) {
		this.intSize = intSize;
	}

	/**
	 * Change the number of bits mapped to a byte. Default this is set to 64.
	 * 
	 * @param longSize
	 */
	public void setLongSize(final int longSize) {
		this.longSize = longSize;
	}

	/**
	 * Change the number of bits mapped to a byte. Default this is set to 16.
	 * 
	 * @param shortSize
	 */
	public void setShortSize(final int shortSize) {
		this.shortSize = shortSize;
	}

	/**
	 * Read a byte and interpret it as a boolean. A non-zero value is
	 * interpreted as true, a zero value as false.
	 * 
	 * @return A byte as a boolean.
	 */
	public boolean readBoolean() {
		return this.readUnsignedByte() != 0;
	}

	/**
	 * Read one, two, four or eight bytes. An attempt to read any other number
	 * of bytes will result in illegal argument exception.
	 * 
	 * @param howMany
	 *            one, two, four or eight
	 * @return A long containing the data of the given number of bytes.
	 */
	private long readBytes(final int howMany) {
		long result = 0;
		switch (howMany) {
		case 1: {
			result = this.getBuffer().get();
			break;
		}
		case 2: {
			result = this.getBuffer().getShort();
			break;
		}
		case 4: {
			result = this.getBuffer().getInt();
			break;
		}
		case 8: {
			result = this.getBuffer().getLong();
			break;
		}
		default: {
			throw new IllegalArgumentException(String.format(
					"Number of bytes to read must be 1,2,4 or 8. Got: %d",
					howMany));
		}
		}

		return result;
	}

	/**
	 * Read a signed byte.
	 * 
	 * @return a signed byte.
	 */
	public int readSignedByte() {
		return (byte) this.readBytes(getByteSize());
	}

	/**
	 * Read a native signed integer.
	 * 
	 * @return A signed integer.
	 */
	public int readSignedInt() {
		return (int) this.readBytes(getIntSize());
	}

	/**
	 * Read a native signed long.
	 * 
	 * @return A signed long.
	 */
	public long readSignedLong() {
		return this.readBytes(getLongSize());
	}

	/**
	 * Read a signed short.
	 * 
	 * @return A signed short.
	 */
	public int readSignedShort() {
		return (short) this.readBytes(getShortSize());
	}

	/**
	 * Read an unsigned byte. A unsigned byte has a length of one byte.
	 * 
	 * @return An unsigned byte, represented by a Java short.
	 */
	public int readUnsignedByte() {
		return (int) (this.readBytes(getByteSize()) & 0xFF);
	}

	/**
	 * Read an unsigned short. A unsigned short has a length of two bytes.
	 * 
	 * @return An unsigned short, represented by a Java integer.
	 */
	public int readUnsignedShort() {
		return (int) (this.readBytes(getShortSize()) & 0xFFFF);
	}

	/**
	 * Read an unsigned integer. A unsigned integer has a length of four bytes.
	 * 
	 * @return An unsigned integer, represented by a Java long.
	 */
	public long readUnsignedInt() {
		return this.readBytes(getIntSize()) & 0xFFFFFFFFl;
	}

}
