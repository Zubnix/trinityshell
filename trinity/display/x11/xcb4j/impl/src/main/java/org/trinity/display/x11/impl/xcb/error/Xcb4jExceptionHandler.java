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
package org.trinity.display.x11.impl.xcb.error;

import java.util.Date;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCallExceptionHandler;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>XcbNativeErrorHandler</code> handles a <code>XNativeCall</code> if an
 * error is returned.
 * <p>
 * A <code>XcbNativeErrorHandler</code> checks the return value of the
 * <code>XNativeCall<code>. If an <code>XNativeCall</code> returns true, the
 * <code>NativeBufferHelper</code> of the <code>XNativeCall</code> will contain
 * the error data. The <code>XcbNativeErrorHandler</code> will read this data
 * and wrap it in an Java <code>Error</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class Xcb4jExceptionHandler implements XCallExceptionHandler {

	@Override
	public <R, D, A> R handleException(	final XCaller xNativeCaller,
										final XCall<R, D, A> erroneousNativeCall,
										final D erroneousDisplayPeer,
										@SuppressWarnings("unchecked") final A... erroneousArgs) {
		final NativeBufferHelper bufferHelper = ((AbstractXcbCall<R, D, A>) erroneousNativeCall)
				.getNativeBufferHelper();
		// final short responseType =
		bufferHelper.readUnsignedByte();
		final int errorCode = bufferHelper.readUnsignedByte();
		// final int sequence =
		bufferHelper.readUnsignedShort();
		final long resourceId = bufferHelper.readUnsignedInt();
		// final int minorCode =
		// bufferHelper.readUnsignedShort();
		// final short majorCode =
		// bufferHelper.readUnsignedByte();

		final XErrorCode xErrorCode = XErrorCodes.getByXErrorIntCode(errorCode);
		bufferHelper.doneReading();

		final BadXcbCall ex = new BadXcbCall(xErrorCode, resourceId, new Date());

		throw ex;
	}
}
