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
import org.trinity.display.x11.api.event.XConfigureEvent;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XConfigureEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public class XConfigureEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.CONFIGURE_NOTIFY);

	private final XResourceFactory xResourceFactory;
	private final DisplayEventFactory displayEventFactory;

	public XConfigureEventConverter(final XResourceFactory xResourceFactory,
									final DisplayEventFactory displayEventFactory) {
		this.xResourceFactory = xResourceFactory;
		this.displayEventFactory = displayEventFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.XEventConverter#getXEventCode()
	 */
	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}

	@Override
	public DisplayEvent convertEvent(final XEvent sourceEvent) {

		final XConfigureEvent event = (XConfigureEvent) sourceEvent;
		final XWindow window = event.getWindow();
		final int x = event.getX();
		final int y = event.getY();
		final int width = event.getwidth();
		final int height = event.getHeight();

		return this.displayEventFactory.createConfigureNotify(	window,
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
		// Contents of native buffer;
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		// xcb_window_t above_sibling; /**< */
		// int16_t x; /**< */
		// int16_t y; /**< */
		// uint16_t width; /**< */
		// uint16_t height; /**< */
		// uint16_t border_width; /**< */
		// uint8_t override_redirect; /**< */
		// uint8_t pad1; /**< */

		rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readSignedShort();
		final int eventId = (int) rawEvent.readUnsignedInt();
		final int windowId = (int) rawEvent.readUnsignedInt();
		final int aboveSiblingId = (int) rawEvent.readUnsignedInt();
		final int x = rawEvent.readSignedShort();
		final int y = rawEvent.readSignedShort();
		final int width = rawEvent.readUnsignedShort();
		final int height = rawEvent.readUnsignedShort();
		final int borderWidth = rawEvent.readUnsignedShort();
		final boolean overrideRedirect = rawEvent.readBoolean();
		rawEvent.doneReading();

		final XWindow window = this.xResourceFactory
				.createXWindow(XResourceHandleImpl.valueOf(windowId));
		final XWindow event = this.xResourceFactory
				.createXWindow(XResourceHandleImpl.valueOf(eventId));
		final XWindow aboveSibling = this.xResourceFactory
				.createXWindow(XResourceHandleImpl.valueOf(aboveSiblingId));

		return new XConfigureEventImpl(	this.eventCode.intValue(),
										sequence,
										event,
										window,
										aboveSibling,
										x,
										y,
										width,
										height,
										borderWidth,
										overrideRedirect);
	}
}