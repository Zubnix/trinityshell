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

public class SelectionClearParser extends AbstractXEventConverterImpl {

	private final XDisplayServer display;

	public SelectionClearParser(final XResourceFactory xResourceFactory,
								final XDisplayServer display) {
		super(xResourceFactory, XProtocolConstants.SELECTION_CLEAR);
		this.display = display;
	}

	@Override
	public XSelectionClearNotifyEvent convertEvent(	final DisplayEventSource eventSource,
													final NativeBufferHelper eventStruct) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_atom_t selection; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final int time = eventStruct.readSignedInt();
		this.display.setLastServerTime(time);
		final int ownerWindowId = (int) eventStruct.readUnsignedInt();
		final XWindow owner = readXWindow(ownerWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = readXAtom(selectionAtomId);

		final XSelectionClearNotifyEvent selectionClearNotifyEvent = new XSelectionClearNotifyEvent(owner,
																									selectionAtom);

		return selectionClearNotifyEvent;
	}
}