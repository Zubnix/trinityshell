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

import org.trinity.display.x11.api.core.XAtom;
import org.trinity.display.x11.api.core.XDisplayServer;
import org.trinity.display.x11.api.core.XProtocolConstants;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEventSource;

public class SelectionNotifyParser extends AbstractXEventConverterImpl {

	private final XDisplayServer display;

	public SelectionNotifyParser(	final XResourceFactory xResourceFactory,
									final XDisplayServer display) {
		super(xResourceFactory, XProtocolConstants.SELECTION_NOTIFY);
		this.display = display;
	}

	@Override
	public XSelectionNotifyEvent convertEvent(	final DisplayEventSource eventSource,
												final NativeBufferHelper eventStruct) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final int time = eventStruct.readSignedInt();
		this.display.setLastServerTime(time);
		final int requestorWindowId = (int) eventStruct.readUnsignedInt();
		final XWindow requestor = readXWindow(requestorWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = readXAtom(selectionAtomId);
		final long targetAtomId = eventStruct.readUnsignedInt();
		final XAtom targetAtom = readXAtom(targetAtomId);
		final long propertyAtomId = eventStruct.readUnsignedInt();
		final XAtom propertyAtom = readXAtom(propertyAtomId);

		final XSelectionNotifyEvent selectionNotifyEvent = new XSelectionNotifyEvent(	requestor,
																						selectionAtom,
																						targetAtom,
																						propertyAtom);

		return selectionNotifyEvent;
	}
}