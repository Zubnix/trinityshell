/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.trinity.display.x11.core.api.XEventBridge;
import org.trinity.display.x11.core.api.XEventConverter;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.DisplayEventQueue;
import org.trinity.foundation.display.api.event.DisplayEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * An <code>XcbEventParser</code> implements the parsing that is needed to read
 * an <code>XcbEvent</code> from a <code>NativeBufferHelper</code>. It does this
 * by implementing an <code>XcbEventParserHelper</code> for every X event type
 * that should be parsed.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@Singleton
public class XEventBridgeImpl implements XEventBridge<NativeBufferHelper> {

	private final Map<Integer, XEventConverter<NativeBufferHelper>> conversionMap = new HashMap<Integer, XEventConverter<NativeBufferHelper>>();
	private final DisplayEventQueue displayEventQueue;

	@Inject
	public XEventBridgeImpl(final Set<XEventConverter<NativeBufferHelper>> parserHelpers,
							final DisplayEventQueue displayEventQueue) {
		this.displayEventQueue = displayEventQueue;
		for (final XEventConverter<NativeBufferHelper> eventParserHelper : parserHelpers) {
			this.conversionMap.put(	eventParserHelper.getXEventCode(),
									eventParserHelper);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.core.display.api.DisplayEventBridge#queueRenderEvent(org.
	 * trinity.core.display.api.event.DisplayEventSource, java.lang.Object)
	 */

	@Override
	public void queueXEvent(final XEvent sourceEvent) {

		final XEventConverter<NativeBufferHelper> xEventConverter = this.conversionMap
				.get(Integer.valueOf(sourceEvent.getEventCode()));

		if (xEventConverter != null) {
			final DisplayEvent displayEvent = xEventConverter
					.convertEvent(sourceEvent);
			this.displayEventQueue.queueDisplayEvent(displayEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.fusion.display.x11.api.XEventBridge#constructXEvent(java.lang.Object)
	 */
	@Override
	public XEvent constructXEvent(final NativeBufferHelper rawEvent) {
		final Integer eventCode = readEventCode(rawEvent);

		final XEventConverter<NativeBufferHelper> xEventConverter = this.conversionMap
				.get(eventCode);
		if (xEventConverter == null) {
			return null;
		}
		return xEventConverter.constructEvent(rawEvent);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.fusion.display.x11.api.XEventBridge#readEventCode(java.lang.Object)
	 */
	private Integer readEventCode(final NativeBufferHelper rawEvent) {
		final int eventCodeByte = rawEvent.readUnsignedByte();
		return Integer.valueOf(eventCodeByte & ~0x80);
	}
}