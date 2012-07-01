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
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public abstract class AbstractXFocusEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode;
	private final XEventFactory xEventFactory;
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;

	public AbstractXFocusEventConverter(final int eventCode,
										final XEventFactory xEventFactory,
										final XDisplayResourceFactory xResourceFactory,
										final XResourceHandleFactory xResourceHandleFactory) {
		this.eventCode = Integer.valueOf(eventCode);
		this.xEventFactory = xEventFactory;
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
	public XEvent constructEvent(final NativeBufferHelper rawEventBuffer) {
		// contents of native buffer:
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */
		final int detail = rawEventBuffer.readUnsignedByte();
		final int sequence = rawEventBuffer.readUnsignedShort();
		final int eventId = (int) rawEventBuffer.readUnsignedInt();
		final int mode = rawEventBuffer.readUnsignedByte();
		rawEventBuffer.doneReading();

		return this.xEventFactory
				.createXFocusEvent(	this.eventCode.intValue(),
									detail,
									sequence,
									this.xDisplayResourceFactory
											.createDisplayRenderArea(this.xResourceHandleFactory
													.createResourceHandle(Integer
															.valueOf(eventId))),
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
