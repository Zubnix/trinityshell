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

import org.trinity.display.x11.api.XEventConverter;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.event.XButtonEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XButtonEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public abstract class AbstractXButtonEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final XResourceFactory xResourceFactory;
	private final XDisplayServer display;

	private final Integer eventCode;

	public AbstractXButtonEventConverter(	final int eventCode,
											final XResourceFactory xResourceFactory,
											final XDisplayServer display) {
		this.eventCode = Integer.valueOf(eventCode);
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
	public XButtonEvent constructEvent(final NativeBufferHelper rawEvent) {
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
		final int detail = rawEvent.readSignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int timestamp = rawEvent.readSignedInt();
		this.display.setLastServerTime(timestamp);
		final int rootId = rawEvent.readSignedInt();
		final int eventWindowId = (int) rawEvent.readUnsignedInt();
		final int childId = rawEvent.readSignedInt();
		final int rootX = rawEvent.readSignedShort();
		final int rootY = rawEvent.readSignedShort();
		final int eventX = rawEvent.readSignedShort();
		final int eventY = rawEvent.readSignedShort();
		final int state = rawEvent.readUnsignedShort();
		final boolean sameScreen = rawEvent.readBoolean();
		rawEvent.doneReading();

		return new XButtonEventImpl(this.eventCode.intValue(),
									sequence,
									this.xResourceFactory
											.createXWindow(XResourceHandleImpl
													.valueOf(eventWindowId)),
									this.xResourceFactory
											.createXWindow(XResourceHandleImpl
													.valueOf(rootId)),
									this.xResourceFactory
											.createXWindow(XResourceHandleImpl
													.valueOf(childId)),
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