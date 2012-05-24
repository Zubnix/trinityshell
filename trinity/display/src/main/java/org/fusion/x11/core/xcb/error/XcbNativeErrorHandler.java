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
package org.fusion.x11.core.xcb.error;

import java.util.Date;

import org.apache.log4j.Logger;
import org.fusion.x11.nativeHelpers.NativeBufferHelper;
import org.fusion.x11.nativeHelpers.NativeErrorHandler;
import org.fusion.x11.nativeHelpers.XNativeCall;
import org.fusion.x11.nativeHelpers.XNativeCaller;

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
public class XcbNativeErrorHandler implements NativeErrorHandler {

	private static final Logger logger = Logger
			.getLogger(XcbNativeErrorHandler.class);
	private static final String ERR_NATIVE_XCB_CALL_LOGSMESSAGE = "Erroneous native Xcb call.";

	@Override
	public <R, D, A> R handleNativeError(final XNativeCaller xNativeCaller,
			final XNativeCall<R, D, A> erroneousNativeCall,
			final D erroneousDisplayPeer, final A... erroneousArgs) {
		final NativeBufferHelper bufferHelper = erroneousNativeCall
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
		bufferHelper.enableWrite();

		BadNativeXcbCall ex = new BadNativeXcbCall(xErrorCode, resourceId,
				new Date());

		logger.error(ERR_NATIVE_XCB_CALL_LOGSMESSAGE, ex);

		throw ex;
	}
}
