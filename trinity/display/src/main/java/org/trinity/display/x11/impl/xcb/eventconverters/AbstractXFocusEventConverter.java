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
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XFocusChangeEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public abstract class AbstractXFocusEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode;
	private final XResourceFactory xResourceFactory;

	public AbstractXFocusEventConverter(final int eventCode,
										final XResourceFactory xResourceFactory) {
		this.eventCode = Integer.valueOf(eventCode);
		this.xResourceFactory = xResourceFactory;
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
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */
		final int detail = rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int eventId = (int) rawEvent.readUnsignedInt();
		final int mode = rawEvent.readUnsignedByte();
		rawEvent.doneReading();
		return new XFocusChangeEventImpl(	this.eventCode.intValue(),
											detail,
											sequence,
											this.xResourceFactory
													.createXWindow(XResourceHandleImpl
															.valueOf(eventId)),
											mode);
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
