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
package org.trinity.display.x11.impl.xcb;

import org.trinity.display.x11.api.core.XCall;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

/**
 * An <code>XNativeCall</code> wraps and and handles a JNI call. It splits the
 * original operation of calling a basic jni method into: implementation through
 * <code>nativeCallImpl()</code> calling through <code>exec</code> and result
 * fetching through <code>getResult()</code>.
 * <p>
 * Programmers implementing a <code>XNativeCall</code> should not call these
 * methods manually but instead use a <code>XNativeCaller</code>.
 * <p>
 * A possible implementation of an XNativeCall might look like this: <code>
 * <pre>
 * 	XNativeCall getRootWindow = new XNativeCall<Long, Long, Long>() {
 * 		public Long getResult() {
 * 			final Long rootId = Long.valueOf(getNativeBufferHelper()
 * 					.readUnsignedInt());
 * 			return rootId;
 * 		}
 * 	
 * 		public boolean nativeCallImpl() {
 * 			final boolean error = XCoreNative.nativeGetRootWindow(
 * 					getDisplayPeer().longValue(), NativeBufferHelper.getStructHelper()
 * 							.getBuffer());
 * 			return error;
 * 		}
 * 	};
 * </pre>
 * </code>
 * <p>
 * Calling the above defined <code>XNativeCall</code> might look like this:
 * <code>
 * <pre>
 *  // get the native pointer of the native display struct.
 * 	final Long displayAddress = display.getNativePeer();
 * 
 *  // call the underlying jni native method with the 'getRootWindow' XNativeCall 
 *  // implementation and the native display pointer as arguments
 * 	final Long windowID = xNativeCaller.doNativeCall(getRootWindow, displayAddress);
 * </pre>
 * </code>
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @param <R>
 *            The return type.
 * @param <D>
 *            The display peer type.
 * @param <A>
 *            The arguments type.
 */
public abstract class AbstractXcbCall<R, D, A> implements XCall<R, D, A> {
	private D xConnectionReference;
	private A[] args;

	/**
	 * Execute the <code>nativeCallImpl()</code> method. Before
	 * <code>nativeCallImpl()</code> is called, the thread local
	 * <code>NativeBufferHelper</code> of this <code>XNativeCall</code> is
	 * cleared. This method should be the preferred way to call
	 * <code>nativeBufferHelper</code>.
	 * 
	 * @return The same value as <code>nativeCallImpl()</code>.
	 */
	@Override
	public boolean exec() {
		final NativeBufferHelper nativeBufferHelper = getNativeBufferHelper();
		if (nativeBufferHelper.isWriteEnabled()) {
			nativeBufferHelper.getBuffer().clear();
			final boolean error = callImpl();
			nativeBufferHelper.getBuffer().compact();
			nativeBufferHelper.getBuffer().flip();
			return error;
		} else {
			throw new Error("Bug! Can not execute native call. Result buffer is not yet marked as done reading.");
		}
	}

	/**
	 * @return
	 */
	@Override
	public A[] getArgs() {
		return this.args;
	}

	/**
	 * @return
	 */
	@Override
	public D getConnectionReference() {
		return this.xConnectionReference;
	}

	/**
	 * The thread local <code>NativeBufferHelper</code> of this
	 * <code>XNativeCall</code>.
	 * 
	 * @return A {@link NativeBufferHelper}.
	 */
	public NativeBufferHelper getNativeBufferHelper() {
		return NativeBufferHelper.getBufferHelper();
	}

	/**
	 * The result that is stored in the thread local
	 * <code>NativeBufferHelper</code> after this <code>XNativeCall</code> is
	 * executed.
	 * <p>
	 * The default implementation of this method returns <code>null</code>.
	 * Programmers wishing to return a result should override this method and
	 * read the result from the <code>NativeBufferHelper</code> of this
	 * <code>XNativeCall</code>.
	 * 
	 * @return a result.
	 */
	@Override
	public R getResult() {
		return null;
	}

	/**
	 * @return
	 */
	protected abstract boolean callImpl();

	/**
	 * Define the arguments that should be used when implementing and executing
	 * this <code>XNativeCall</code>.
	 * 
	 * @param args
	 *            A number of arguments.
	 */
	@Override
	public void setArgs(final A... args) {
		this.args = args;
	}

	/**
	 * Define the display peer that should be used when implementing and
	 * executing this <code>XNativeCall</code>.
	 * 
	 * @param displayPeer
	 *            A display peer.
	 */
	@Override
	public void setConnectionReference(final D displayPeer) {
		this.xConnectionReference = displayPeer;
	}
}
