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

import org.trinity.display.x11.api.core.XAtomFactory;
import org.trinity.display.x11.api.core.XDisplayResourceFactory;
import org.trinity.display.x11.api.core.XDisplayServer;
import org.trinity.display.x11.api.core.XEventConverter;
import org.trinity.display.x11.api.core.XProtocolConstants;
import org.trinity.display.x11.api.core.XResourceHandleFactory;
import org.trinity.display.x11.api.core.event.XEvent;
import org.trinity.display.x11.api.core.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;

import com.google.inject.Inject;

public class XSelectionRequestEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.SELECTION_REQUEST);

	private final XDisplayServer xDisplay;
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XAtomFactory xAtomFactory;
	private final XEventFactory xEventFactory;

	@Inject
	public XSelectionRequestEventConverter(	final XDisplayResourceFactory xDisplayResourceFactory,
											final XResourceHandleFactory xResourceHandleFactory,
											final XDisplayServer xDisplay,
											final XAtomFactory xAtomFactory,
											final XEventFactory xEventFactory) {
		this.xDisplay = xDisplay;
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xAtomFactory = xAtomFactory;
		this.xEventFactory = xEventFactory;
	}

	@Override
	public XEvent constructEvent(final NativeBufferHelper eventStruct) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		final int sequence = eventStruct.readUnsignedShort();
		final int time = eventStruct.readSignedInt();
		this.xDisplay.setLastServerTime(time);

		final int ownerwId = (int) eventStruct.readUnsignedInt();
		final int requestorId = (int) eventStruct.readUnsignedInt();
		final int selectionId = (int) eventStruct.readUnsignedInt();
		final int targetId = (int) eventStruct.readUnsignedInt();
		final int propertyId = (int) eventStruct.readUnsignedInt();
		eventStruct.doneReading();

		return this.xEventFactory
				.createXSelectionRequestEvent(	this.eventCode.intValue(),
												sequence,
												time,
												this.xDisplayResourceFactory
														.createPlatformRenderArea(this.xResourceHandleFactory
																.createResourceHandle(Integer
																		.valueOf(ownerwId))),
												this.xDisplayResourceFactory
														.createPlatformRenderArea(this.xResourceHandleFactory
																.createResourceHandle(Integer
																		.valueOf(requestorId))),
												this.xAtomFactory
														.createAtom(selectionId),
												this.xAtomFactory
														.createAtom(targetId),
												this.xAtomFactory
														.createAtom(propertyId));
	}

	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}

	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {

		return null;
	}
}