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
import org.trinity.display.x11.api.core.XDisplayServer;
import org.trinity.display.x11.api.core.XEventConverter;
import org.trinity.display.x11.api.core.XResourceHandleFactory;
import org.trinity.display.x11.api.core.event.XEvent;
import org.trinity.display.x11.api.core.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public abstract class AbstractXKeyEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode;
	private final XEventFactory xEventFactory;
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XDisplayServer xDisplayServer;

	public AbstractXKeyEventConverter(	final int eventCode,
										final XEventFactory xEventFactory,
										final XDisplayServer xDisplayServer,
										final XDisplayResourceFactory xResourceFactory,
										final XResourceHandleFactory xResourceHandleFactory) {
		this.eventCode = eventCode;
		this.xEventFactory = xEventFactory;
		this.xDisplayServer = xDisplayServer;
		this.xDisplayResourceFactory = xResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#constructEvent(java.lang.
	 * Object)
	 */
	@Override
	public XEvent constructEvent(final NativeBufferHelper rawEvent) {
		// contents of native buffer:
		// xcb_keycode_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t root; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t child; /**< */
		// int16_t root_x; /**< */
		// int16_t root_y; /**< */
		// int16_t event_x; /**< */
		// int16_t event_y; /**< */
		// uint16_t state; /**< */
		// uint8_t same_screen; /**< */
		// uint8_t pad0; /**< */
		final int detail = rawEvent.readSignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int time = rawEvent.readSignedInt();
		this.xDisplayServer.setLastServerTime(time);
		final int rootId = rawEvent.readSignedInt();
		final int eventId = (int) rawEvent.readUnsignedInt();
		final int childId = (int) rawEvent.readUnsignedInt();
		final int rootX = rawEvent.readSignedShort();
		final int rootY = rawEvent.readSignedShort();
		final int eventX = rawEvent.readSignedShort();
		final int eventY = rawEvent.readSignedShort();
		final int state = rawEvent.readUnsignedShort();
		final boolean sameScreen = rawEvent.readBoolean();
		rawEvent.doneReading();

		return this.xEventFactory
				.createXKeyEvent(	this.eventCode.intValue(),
									detail,
									sequence,
									this.xDisplayResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(rootId))),
									this.xDisplayResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(eventId))),
									this.xDisplayResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(childId))),
									time,
									rootX,
									rootY,
									eventX,
									eventY,
									state,
									sameScreen);
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
