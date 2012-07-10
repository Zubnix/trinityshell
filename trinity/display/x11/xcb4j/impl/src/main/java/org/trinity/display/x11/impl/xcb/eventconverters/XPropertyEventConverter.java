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
import org.trinity.display.x11.core.api.XDisplayServer;
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
public class XPropertyEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.PROPERTY_NOTIFY);

	private final XDisplayServer display;
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XEventFactory xEventFactory;
	private final XAtomFactory xAtomFactory;

	@Inject
	public XPropertyEventConverter(	final XEventFactory xEventFactory,
									final XDisplayResourceFactory xDisplayResourceFactory,
									final XResourceHandleFactory xResourceHandleFactory,
									final XDisplayServer display,
									final XAtomFactory xAtomFactory) {
		this.display = display;
		this.xAtomFactory = xAtomFactory;
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xEventFactory = xEventFactory;
	}

	@Override
	public XEvent constructEvent(final NativeBufferHelper eventStruct) {
		// Contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t window; /**< */
		// xcb_atom_t atom; /**< */
		// xcb_timestamp_t time; /**< */
		// uint8_t state; /**< */
		// uint8_t pad1[3]; /**< */

		eventStruct.readUnsignedByte();
		final int sequence = eventStruct.readUnsignedShort();
		final int windowId = (int) eventStruct.readUnsignedInt();
		final int atomId = (int) eventStruct.readUnsignedInt();
		final int time = (int) eventStruct.readUnsignedInt();
		this.display.setLastServerTime(time);
		final boolean state = eventStruct.readBoolean();
		eventStruct.doneReading();

		return this.xEventFactory
				.createXPropertyEvent(	this.eventCode.intValue(),
										sequence,
										this.xDisplayResourceFactory
												.createDisplayRenderArea(this.xResourceHandleFactory
														.createResourceHandle(Integer
																.valueOf(windowId))),
										this.xAtomFactory.createAtom(atomId),
										time,
										state);
	}

	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		return null;
	}

	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}
}