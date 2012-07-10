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
import org.trinity.display.x11.core.api.XProtocolConstants;
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XDestroyWindowEvent;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class XDestroyWindowEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.DESTROY_NOTIFY);
	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final DisplayEventFactory displayEventFactory;
	private final XEventFactory xEventFactory;

	@Inject
	public XDestroyWindowEventConverter(final XEventFactory xEventFactory,
										final XDisplayResourceFactory xResourceFactory,
										final XResourceHandleFactory xResourceHandleFactory,
										final DisplayEventFactory displayEventFactory) {
		this.xDisplayResourceFactory = xResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.displayEventFactory = displayEventFactory;
		this.xEventFactory = xEventFactory;
	}

	@Override
	public DisplayEvent convertEvent(final XEvent sourceEvent) {

		final XDestroyWindowEvent event = (XDestroyWindowEvent) sourceEvent;
		final XWindow window = event.getWindow();
		return this.displayEventFactory.createDestroyNotify(window);
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
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		rawEventBuffer.readUnsignedByte();
		final int sequence = rawEventBuffer.readUnsignedShort();
		final int eventId = (int) rawEventBuffer.readUnsignedInt();
		final int windowId = (int) rawEventBuffer.readUnsignedInt();
		rawEventBuffer.doneReading();

		return this.xEventFactory
				.createXDestroyWindowEvent(	this.eventCode.intValue(),
											sequence,
											this.xDisplayResourceFactory
													.createDisplayRenderArea(this.xResourceHandleFactory
															.createResourceHandle(Integer
																	.valueOf(eventId))),
											this.xDisplayResourceFactory
													.createDisplayRenderArea(this.xResourceHandleFactory
															.createResourceHandle(Integer
																	.valueOf(windowId))));
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