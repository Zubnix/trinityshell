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
import org.trinity.display.x11.api.core.event.XButtonEvent;
import org.trinity.display.x11.api.core.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public abstract class AbstractXButtonEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final XResourceHandleFactory xResourceHandleFactory;
	private final XDisplayResourceFactory xResourceFactory;
	private final XDisplayServer display;
	private final XEventFactory xEventFactory;

	private final Integer eventCode;

	public AbstractXButtonEventConverter(	final int eventCode,
											final XEventFactory xEventFactory,
											final XResourceHandleFactory xResourceHandleFactory,
											final XDisplayResourceFactory xResourceFactory,
											final XDisplayServer display) {
		this.eventCode = Integer.valueOf(eventCode);
		this.xEventFactory = xEventFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xResourceFactory = xResourceFactory;
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.XEventConverter#getXEventCode()
	 */
	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.fusion.display.x11.api.XEventConverter#constructEvent(java.lang.Object
	 * )
	 */
	@Override
	public XButtonEvent constructEvent(final NativeBufferHelper rawEventBuffer) {
		// Contents of native buffer:
		// xcb_button_t detail
		// uint16_t sequence
		// xcb_timestamp_t time
		// xcb_window_t root
		// xcb_window_t event
		// xcb_window_t child
		// int16_t root_x
		// int16_t root_y
		// int16_t event_x
		// int16_t event_y
		// uint16_t state
		// uint8_t same_screen
		final int detail = rawEventBuffer.readSignedByte();
		final int sequence = rawEventBuffer.readUnsignedShort();
		final int timestamp = rawEventBuffer.readSignedInt();
		this.display.setLastServerTime(timestamp);
		final int rootId = rawEventBuffer.readSignedInt();
		final int eventWindowId = (int) rawEventBuffer.readUnsignedInt();
		final int childId = rawEventBuffer.readSignedInt();
		final int rootX = rawEventBuffer.readSignedShort();
		final int rootY = rawEventBuffer.readSignedShort();
		final int eventX = rawEventBuffer.readSignedShort();
		final int eventY = rawEventBuffer.readSignedShort();
		final int state = rawEventBuffer.readUnsignedShort();
		final boolean sameScreen = rawEventBuffer.readBoolean();
		rawEventBuffer.doneReading();

		return this.xEventFactory
				.createXButtonEvent(this.eventCode.intValue(),
									sequence,
									this.xResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(eventWindowId))),
									this.xResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(rootId))),
									this.xResourceFactory
											.createPlatformRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(childId))),
									timestamp,
									eventX,
									eventY,
									rootX,
									rootY,
									state,
									detail,
									sameScreen);
	}
}