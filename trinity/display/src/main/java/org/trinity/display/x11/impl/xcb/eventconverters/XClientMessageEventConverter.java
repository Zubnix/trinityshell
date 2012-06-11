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

import org.fusion.x11.core.XAtom;
import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.display.x11.api.XEventConverter;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XClientMessageEvent;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

import com.google.inject.Inject;

public class XClientMessageEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.CLIENT_MESSAGE);
	private final XResourceFactory xResourceFactory;
	private final DisplayEventFactory displayEventFactory;

	@Inject
	public XClientMessageEventConverter(final XResourceFactory xResourceFactory,
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
		final XClientMessageEvent event = (XClientMessageEvent) sourceEvent;

		return this.displayEventFactory.createClientMessage(eventWindow,
															messageAtom,
															format,
															data);
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
		// uint8_t format; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t window; /**< */
		// xcb_atom_t type; /**< */
		// xcb_client_message_data_t data; /**< */

		final int format = rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int window = (int) rawEvent.readUnsignedInt();
		final long type = rawEvent.readUnsignedInt();
		final byte[] data = new byte[20];
		rawEvent.getBuffer().get(data);
		rawEvent.doneReading();

		final XWindow eventWindow = this.xResourceFactory
				.createXWindow(XResourceHandleImpl.valueOf(window));

		final XAtom messageAtom = readXAtom(type);

	}
}