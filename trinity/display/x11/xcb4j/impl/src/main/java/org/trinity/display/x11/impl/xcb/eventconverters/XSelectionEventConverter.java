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

@Singleton
public class XSelectionEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.SELECTION_NOTIFY);

	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XEventFactory xEventFactory;
	private final XDisplayServer display;
	private final XAtomFactory xAtomFactory;

	@Inject
	public XSelectionEventConverter(final XDisplayResourceFactory xDisplayResourceFactory,
									final XResourceHandleFactory xResourceHandleFactory,
									final XEventFactory xEventFactory,
									final XDisplayServer display,
									final XAtomFactory xAtomFactory) {
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xEventFactory = xEventFactory;
		this.display = display;
		this.xAtomFactory = xAtomFactory;
	}

	@Override
	public XEvent constructEvent(final NativeBufferHelper eventStruct) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		final int sequence = eventStruct.readUnsignedShort();
		final int time = (int) eventStruct.readUnsignedInt();
		this.display.setLastServerTime(time);
		final int requestorWindowId = (int) eventStruct.readUnsignedInt();
		final int selectionAtomId = (int) eventStruct.readUnsignedInt();
		final int targetAtomId = (int) eventStruct.readUnsignedInt();
		final int propertyAtomId = (int) eventStruct.readUnsignedInt();
		eventStruct.doneReading();

		return this.xEventFactory
				.createXSelectionEvent(	this.eventCode.intValue(),
										sequence,
										time,
										this.xDisplayResourceFactory
												.createDisplayRenderArea(this.xResourceHandleFactory
														.createResourceHandle(Integer
																.valueOf(requestorWindowId))),
										this.xAtomFactory
												.createAtom(selectionAtomId),
										this.xAtomFactory
												.createAtom(targetAtomId),
										this.xAtomFactory
												.createAtom(propertyAtomId));
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