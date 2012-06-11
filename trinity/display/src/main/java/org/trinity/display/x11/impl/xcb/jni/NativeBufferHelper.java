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
package org.trinity.display.x11.impl.xcb.jni;

import java.nio.ByteBuffer;

import org.fusion.x11.core.FlexDataContainer;

// TODO documentation
/**
 * A <code>NativeBufferHelper</code> wraps a <code>DirectByteBuffer</code> and
 * provides a number of helper method to assist in reading primitive data.
 * <p>
 * A <code>NativeBufferHelper</code> can be instantiated by a call to
 * {@link NativeBufferHelper#getBufferHelper()}. This will provide a thread
 * local instance.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class NativeBufferHelper extends FlexDataContainer {
	private static final int MAX_ARGUMENTS_BYTE_SIZE = 16384;

	private final Thread owningThread;

	private boolean doneReading;

	private static final ThreadLocal<NativeBufferHelper> THREAD_LOCAL_BUFFER_HELPER = new ThreadLocal<NativeBufferHelper>() {
		@Override
		public NativeBufferHelper initialValue() {
			return new NativeBufferHelper();
		}
	};

	private static final int DEFAULT_NATIVE_BYTE_SIZE = 1;
	private static final int DEFAULT_NATIVE_SHORT_SIZE = 2;
	private static final int DEFAULT_NATIVE_INT_SIZE = 4;
	private static final int DEFAULT_NATIVE_LONG_SIZE = 8;

	/**
     *
     */
	private NativeBufferHelper() {
		super(ByteBuffer
				.allocateDirect(NativeBufferHelper.MAX_ARGUMENTS_BYTE_SIZE));
		this.owningThread = Thread.currentThread();
		setByteSize(NativeBufferHelper.DEFAULT_NATIVE_BYTE_SIZE);
		setShortSize(NativeBufferHelper.DEFAULT_NATIVE_SHORT_SIZE);
		setIntSize(NativeBufferHelper.DEFAULT_NATIVE_INT_SIZE);
		setLongSize(NativeBufferHelper.DEFAULT_NATIVE_LONG_SIZE);
		doneReading();
	}

	/**
	 * 
	 */
	private void checkThread() {
		if (!Thread.currentThread().equals(this.owningThread)) {
			throw new Error(
					String.format(
							"The current thread %s is not the owner thread: %s of object %s",
							Thread.currentThread(), this.owningThread,
							toString()));
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean isWriteEnabled() {
		checkThread();
		return this.doneReading;
	}

	/**
	 * 
	 * @param doneReading
	 */
	void setDoneReading(final boolean doneReading) {
		checkThread();
		this.doneReading = doneReading;
	}

	/**
	 * 
	 */
	public void doneReading() {
		setDoneReading(true);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public ByteBuffer getBuffer() {
		checkThread();
		return super.getBuffer();
	}

	@Override
	public byte[] getAllData() {
		final byte[] allData = new byte[NativeBufferHelper.MAX_ARGUMENTS_BYTE_SIZE];
		getBuffer().get(allData);
		return allData;
	}

	/**
	 * 
	 * @return
	 */
	public static NativeBufferHelper getBufferHelper() {
		return NativeBufferHelper.THREAD_LOCAL_BUFFER_HELPER.get();
	}
}