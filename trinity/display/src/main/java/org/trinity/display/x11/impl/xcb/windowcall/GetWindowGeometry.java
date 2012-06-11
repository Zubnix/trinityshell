/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.windowcall;

import org.fusion.x11.core.XWindowGeometry;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.display.x11.impl.xcb.jni.Xcb4J;

import com.google.inject.Singleton;

/**
 * args: (Long) window id
 * <p>
 * return: (XWindowGeometry) window geometry
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public class GetWindowGeometry extends
		AbstractXcbCall<XWindowGeometry, Long, Integer> {

	@Override
	public XWindowGeometry getResult() {

		// Contents of native buffer:
		// uint8_t response_type; /**< */
		// uint8_t depth; /**< */
		// uint16_t sequence; /**< */
		// uint32_t length; /**< */
		// xcb_window_t root; /**< */
		// int16_t x; /**< */
		// int16_t y; /**< */
		// uint16_t width; /**< */
		// uint16_t height; /**< */
		// uint16_t border_width; /**< */
		// uint8_t pad0[2]; /**< */

		final NativeBufferHelper nativeBufferHelper = getNativeBufferHelper();
		// final short responseType =
		nativeBufferHelper.readUnsignedByte();
		// final short depth =
		nativeBufferHelper.readUnsignedByte();
		// final int sequence =
		nativeBufferHelper.readUnsignedShort();
		// final long lenght =
		nativeBufferHelper.readUnsignedInt();
		// final long root =
		nativeBufferHelper.readUnsignedInt();
		final int x = nativeBufferHelper.readSignedShort();
		final int y = nativeBufferHelper.readSignedShort();
		final int width = nativeBufferHelper.readUnsignedShort();
		final int height = nativeBufferHelper.readUnsignedShort();
		final int borderWidth = nativeBufferHelper.readUnsignedShort();
		nativeBufferHelper.doneReading();

		final XWindowGeometry wg = new XWindowGeometry(x, y, width
				+ borderWidth, height + borderWidth, borderWidth);

		return wg;
	}

	@Override
	public boolean callImpl() {
		return Xcb4J.nativeGetCurrentWindowGeometry(getConnectionReference()
				.longValue(), getArgs()[0].intValue(), getNativeBufferHelper()
				.getBuffer());
	}
}