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

import org.trinity.display.x11.api.core.XDisplayResourceFactory;
import org.trinity.display.x11.api.core.XEventConverter;
import org.trinity.display.x11.api.core.XProtocolConstants;
import org.trinity.display.x11.api.core.XResourceHandleFactory;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.core.event.XEvent;
import org.trinity.display.x11.api.core.event.XEventFactory;
import org.trinity.display.x11.api.core.event.XUnmapEvent;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventFactory;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XUnmapEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.UNMAP_NOTIFY);

	private final DisplayEventFactory displayEventFactory;
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XEventFactory xEventFactory;

	public XUnmapEventConverter(final DisplayEventFactory displayEventFactory,
								final XEventFactory xEventFactory,
								final XDisplayResourceFactory xDisplayResourceFactory,
								final XResourceHandleFactory xResourceHandleFactory) {
		this.displayEventFactory = displayEventFactory;
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xEventFactory = xEventFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#constructEvent(java.lang.
	 * Object)
	 */
	@Override
	public XEvent constructEvent(final NativeBufferHelper rawEvent) {
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t event
		// xcb_window_t window
		// uint8_t from_configure

		rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int eventId = (int) rawEvent.readUnsignedInt();
		final int windowId = (int) rawEvent.readUnsignedInt();
		final boolean fromConfigure = rawEvent.readBoolean();
		rawEvent.doneReading();

		return this.xEventFactory
				.createXUnmapEvent(	this.eventCode.intValue(),
									sequence,
									this.xDisplayResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(eventId))),
									this.xDisplayResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(windowId))),
									fromConfigure);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#convertEvent(org.trinity.
	 * display.x11.api.event.XEvent)
	 */
	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		final XUnmapEvent event = (XUnmapEvent) xEvent;
		final XWindow window = event.getWindow();
		return this.displayEventFactory.createUnmappedNotify(window);
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