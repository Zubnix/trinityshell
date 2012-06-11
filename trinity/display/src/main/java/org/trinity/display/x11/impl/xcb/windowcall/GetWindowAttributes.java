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

import org.fusion.x11.core.XWindowAttributes;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;
import org.trinity.display.x11.impl.xcb.jni.Xcb4J;

import com.google.inject.Singleton;

/**
 * args: (Long) window id
 * <p>
 * return: (XWindowAttributes) window attributes
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public class GetWindowAttributes extends
		AbstractXcbCall<XWindowAttributes, Long, Integer> {

	@Override
	public XWindowAttributes getResult() {
		// uint8_t response_type; /**< */
		// uint8_t backing_store; /**< */
		// uint16_t sequence; /**< */
		// uint32_t length; /**< */
		// xcb_visualid_t visual; /**< */
		// uint16_t _class; /**< */
		// uint8_t bit_gravity; /**< */
		// uint8_t win_gravity; /**< */
		// uint32_t backing_planes; /**< */
		// uint32_t backing_pixel; /**< */
		// uint8_t save_under; /**< */
		// uint8_t map_is_installed; /**< */
		// uint8_t map_state; /**< */
		// uint8_t override_redirect; /**< */
		// xcb_colormap_t colormap; /**< */
		// uint32_t all_event_masks; /**< */
		// uint32_t your_event_mask; /**< */
		// uint16_t do_not_propagate_mask; /**< */
		// uint8_t pad0[2]; /**< */

		getNativeBufferHelper().readUnsignedByte(); // uint8_t
		// response_type
		getNativeBufferHelper().readUnsignedByte();// uint8_t
		// backing_store
		getNativeBufferHelper().readUnsignedShort();// uint16_t
		// sequence
		getNativeBufferHelper().readUnsignedInt();// uint32_t
		// length
		final long visualPeer = getNativeBufferHelper().readUnsignedInt();// xcb_visualid_t
		// visual
		getNativeBufferHelper().readUnsignedShort();// uint16_t
		// _class
		getNativeBufferHelper().readUnsignedByte();// uint8_t
		// bit_gravity
		getNativeBufferHelper().readUnsignedByte();// uint8_t
		// win_gravity
		getNativeBufferHelper().readUnsignedInt();// uint32_t
		// backing_planes
		getNativeBufferHelper().readUnsignedInt();// uint32_t
		// backing_pixel
		getNativeBufferHelper().readUnsignedByte();// uint8_t
		// save_under
		getNativeBufferHelper().readUnsignedByte();// uint8_t
		// map_is_installed
		final int mapState = getNativeBufferHelper().readUnsignedByte();// uint8_t
		// map_state
		final boolean overrideRedirect = getNativeBufferHelper().readBoolean();// uint8_t
		// override_redirect
		getNativeBufferHelper().readUnsignedInt();// xcb_colormap_t
		// colormap
		getNativeBufferHelper().readUnsignedInt();// uint32_t
		// all_event_masks
		getNativeBufferHelper().readUnsignedInt();// uint32_t
		// your_event_mask
		getNativeBufferHelper().readUnsignedShort();// uint16_t
		// do_not_propagate_mask
		// pad
		getNativeBufferHelper().doneReading();
		final XWindowAttributes wa = new XWindowAttributes(	mapState,
															overrideRedirect,
															visualPeer);

		return wa;
	}

	@Override
	public boolean callImpl() {
		return Xcb4J.nativeGetWindowAttributes(getConnectionReference()
				.longValue(), getArgs()[0].intValue(), getNativeBufferHelper()
				.getBuffer());
	}
}