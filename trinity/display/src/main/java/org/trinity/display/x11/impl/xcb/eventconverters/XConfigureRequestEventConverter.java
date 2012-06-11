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
package org.trinity.display.x11.impl.xcb.eventconverters;

import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.display.x11.api.XEventConverter;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XConfigureRequestEvent;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XConfigureRequestEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

import com.google.inject.Inject;

public class XConfigureRequestEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.CONFIGURE_REQUEST);

	private final DisplayEventFactory displayEventFactory;
	private final XResourceFactory xResourceFactory;

	@Inject
	public XConfigureRequestEventConverter(	final XResourceFactory xResourceFactory,
											final DisplayEventFactory displayEventFactory) {
		this.xResourceFactory = xResourceFactory;
		this.displayEventFactory = displayEventFactory;
	}

	@Override
	public DisplayEvent convertEvent(final XEvent sourceEvent) {
		final XConfigureRequestEvent event = (XConfigureRequestEvent) sourceEvent;
		final XWindow window = event.getWindow();
		final int x = event.getX();
		final int y = event.getY();
		final int width = event.getWidth() + event.getBorderWidth();
		final int height = event.getHeight() + event.getBorderWidth();
		final boolean xSet = (event.getValueMask() & XProtocolConstants.CONFIG_WINDOW_X) != 0;
		final boolean ySet = (event.getValueMask() & XProtocolConstants.CONFIG_WINDOW_Y) != 0;
		final boolean heightSet = (event.getValueMask() & XProtocolConstants.CONFIG_WINDOW_WIDTH) != 0;
		final boolean widthSet = (event.getValueMask() & XProtocolConstants.CONFIG_WINDOW_HEIGHT) != 0;
		return this.displayEventFactory.createConfigureRequest(	window,
																xSet,
																ySet,
																heightSet,
																widthSet,
																x,
																y,
																width,
																height);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#constructEvent(java.lang.
	 * Object)
	 */
	@Override
	public XEvent constructEvent(final NativeBufferHelper rawEvent) {
		// Contents of native buffer:
		// uint8_t stack_mode; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t parent; /**< */
		// xcb_window_t window; /**< */
		// xcb_window_t sibling; /**< */
		// int16_t x; /**< */
		// int16_t y; /**< */
		// uint16_t width; /**< */
		// uint16_t height; /**< */
		// uint16_t border_width; /**< */
		// uint16_t value_mask; /**< */
		final int stackMode = rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int parentId = (int) rawEvent.readUnsignedInt();
		final int windowId = (int) rawEvent.readUnsignedInt();
		final int siblingId = (int) rawEvent.readUnsignedInt();
		final int x = rawEvent.readSignedShort();
		final int y = rawEvent.readSignedShort();
		final int width = rawEvent.readUnsignedShort();
		final int height = rawEvent.readUnsignedShort();
		final int borderWidth = rawEvent.readUnsignedShort();
		final int valueMask = rawEvent.readUnsignedShort();
		rawEvent.doneReading();

		return new XConfigureRequestEventImpl(	this.eventCode.intValue(),
												stackMode,
												sequence,
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(parentId)),
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(windowId)),
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(siblingId)),
												x,
												y,
												width,
												height,
												borderWidth,
												valueMask);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.XEventConverter#getXEventCode()
	 */
	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}
}