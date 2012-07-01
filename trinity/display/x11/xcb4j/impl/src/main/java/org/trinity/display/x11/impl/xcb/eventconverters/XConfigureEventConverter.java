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

import org.trinity.display.x11.core.api.XDisplayResourceFactory;
import org.trinity.display.x11.core.api.XEventConverter;
import org.trinity.display.x11.core.api.XProtocolConstants;
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XConfigureEvent;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventFactory;

public class XConfigureEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.CONFIGURE_NOTIFY);

	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XEventFactory xEventFactory;
	private final DisplayEventFactory displayEventFactory;

	public XConfigureEventConverter(final XDisplayResourceFactory xResourceFactory,
									final XResourceHandleFactory xResourceHandleFactory,
									final XEventFactory xEventFactory,
									final DisplayEventFactory displayEventFactory) {
		this.xDisplayResourceFactory = xResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.displayEventFactory = displayEventFactory;
		this.xEventFactory = xEventFactory;
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

		final XWindow window = this.xDisplayResourceFactory
				.createDisplayRenderArea(this.xResourceHandleFactory
						.createResourceHandle(Integer.valueOf(windowId)));
		final XWindow event = this.xDisplayResourceFactory
				.createDisplayRenderArea(this.xResourceHandleFactory
						.createResourceHandle(Integer.valueOf(eventId)));
		final XWindow aboveSibling = this.xDisplayResourceFactory
				.createDisplayRenderArea(this.xResourceHandleFactory
						.createResourceHandle(Integer.valueOf(aboveSiblingId)));

		return this.xEventFactory.createXConfigureEvent(this.eventCode
																.intValue(),
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