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

import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.XEventConverter;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XPointerVisitationEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public abstract class AbstractXPointerVisitationConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode;

	private final XResourceFactory xResourceFactory;
	private final XDisplayServer display;

	public AbstractXPointerVisitationConverter(	final int eventCode,
												final XResourceFactory xResourceFactory,
												final XDisplayServer display) {
		this.eventCode = Integer.valueOf(eventCode);
		this.display = display;
		this.xResourceFactory = xResourceFactory;
	}

	@Override
	public XEvent constructEvent(final NativeBufferHelper eventStruct) {
		// Contents of native buffer:
		// uint8_t detail;
		// uint16_t sequence;
		// xcb_timestamp_t time; /* Time, in milliseconds the event took
		// place
		// in */
		// xcb_window_t root;
		// xcb_window_t event;
		// xcb_window_t child;
		// int16_t root_x;
		// int16_t root_y;
		// int16_t event_x; /* The x coordinate of the mouse when the event
		// was
		// generated */
		// int16_t event_y; /* The y coordinate of the mouse when the event
		// was
		// generated */
		// uint16_t state; /* A mask of the buttons (or keys) during the
		// event
		// */
		// uint8_t mode; /* The number of mouse button that was clicked */
		// uint8_t same_screen_focus;

		final int detail = eventStruct.readUnsignedByte();
		final int sequence = eventStruct.readUnsignedShort();
		final int timestamp = (int) eventStruct.readUnsignedInt();
		this.display.setLastServerTime(timestamp);
		final int rootId = (int) eventStruct.readUnsignedInt();
		final int eventId = (int) eventStruct.readUnsignedInt();
		final int childId = (int) eventStruct.readUnsignedInt();
		final int rootX = eventStruct.readSignedInt();
		final int rootY = eventStruct.readSignedInt();
		final int eventX = eventStruct.readSignedInt();
		final int eventY = eventStruct.readSignedInt();
		final int state = eventStruct.readUnsignedShort();
		final int mode = eventStruct.readUnsignedByte();
		final boolean sameScreen = eventStruct.readBoolean();

		return new XPointerVisitationEventImpl(	this.eventCode,
												detail,
												sequence,
												timestamp,
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(rootId)),
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(eventId)),
												this.xResourceFactory
														.createXWindow(XResourceHandleImpl
																.valueOf(childId)),
												rootX,
												rootY,
												eventX,
												eventY,
												state,
												mode,
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