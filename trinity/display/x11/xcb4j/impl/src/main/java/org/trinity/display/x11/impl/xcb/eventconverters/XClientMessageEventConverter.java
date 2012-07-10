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

import org.trinity.display.x11.core.api.XAtomFactory;
import org.trinity.display.x11.core.api.XDisplayResourceFactory;
import org.trinity.display.x11.core.api.XEventConverter;
import org.trinity.display.x11.core.api.XProtocolConstants;
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class XClientMessageEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.CLIENT_MESSAGE);

	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XAtomFactory xAtomFactory;
	private final XEventFactory xEventFactory;

	// private final DisplayEventFactory displayEventFactory;

	@Inject
	public XClientMessageEventConverter(final XDisplayResourceFactory xDisplayResourceFactory,
										final XResourceHandleFactory xResourceHandleFactory,
										final XAtomFactory xAtomFactory,
										final XEventFactory xEventFactory) {
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xAtomFactory = xAtomFactory;
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
		return null;
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
		final int windowId = (int) rawEvent.readUnsignedInt();
		final int typeId = (int) rawEvent.readUnsignedInt();
		final byte[] data = new byte[20];
		rawEvent.getBuffer().get(data);
		rawEvent.doneReading();

		return this.xEventFactory
				.createXClientMessageEvent(	this.eventCode.intValue(),
											format,
											sequence,
											this.xDisplayResourceFactory
													.createDisplayRenderArea(this.xResourceHandleFactory
															.createResourceHandle(Integer
																	.valueOf(windowId))),
											this.xAtomFactory
													.createAtom(typeId),
											data);
	}
}