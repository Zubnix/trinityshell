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
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.PropertyChangedNotifyEvent;
import org.trinity.core.display.api.property.Property;
import org.trinity.core.display.api.property.PropertyInstance;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.impl.AbstractXEventConverterImpl;
import org.trinity.display.x11.impl.property.AbstractXProperty;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public class PropertyNotifyParser extends AbstractXEventConverterImpl {

	private final XDisplayServer display;
	private final DisplayEventFactory displayEventFactory;

	public PropertyNotifyParser(final XResourceFactory xResourceFactory,
								final XDisplayServer display,
								final DisplayEventFactory displayEventFactory) {
		super(xResourceFactory, XProtocolConstants.PROPERTY_NOTIFY);
		this.display = display;
		this.displayEventFactory = displayEventFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PropertyChangedNotifyEvent convertEvent(	final DisplayEventSource eventSource,
													final NativeBufferHelper eventStruct) {
		// Contents of native buffer:
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t window
		// xcb_atom_t atom
		// xcb_timestamp_t time
		// uint8_t state
		// uint8_t pad1[3]

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final int windowId = (int) eventStruct.readUnsignedInt();
		final long atomId = eventStruct.readUnsignedInt();
		final int time = eventStruct.readSignedInt();
		this.display.setLastServerTime(time);
		final int state = eventStruct.readUnsignedByte();
		eventStruct.doneReading();

		final XWindow eventWindow = readXWindow(windowId);

		final XAtom atom = readXAtom(atomId);

		if (atom instanceof AbstractXProperty<?>) {
			return this.displayEventFactory
					.createPropertyChangedNotify(	eventWindow,
													state == XProtocolConstants.PROPERTY_DELETE,
													(Property<? extends PropertyInstance>) atom);
		} else {
			return null;
		}
	}
}