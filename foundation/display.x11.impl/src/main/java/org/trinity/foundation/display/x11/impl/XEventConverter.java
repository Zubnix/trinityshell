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
package org.trinity.foundation.display.x11.impl;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XEventConversion;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public final class XEventConverter {

	/*
	 * Singletons are eagerly created in Guice by default. So even though nobody
	 * needs an instance, it's still created anyway. In the case of
	 * XEventConverter, when Guice creates it (at binding time at startup), it
	 * immediately subscribes itself to the XEventBus and starts processing x
	 * events in it's @Subscribe method as soon as events arrive.
	 */

	private final Map<Integer, XEventConversion> conversionMap = new HashMap<Integer, XEventConversion>();

	@Inject
	XEventConverter(final Set<XEventConversion> eventConversions,
					@XEventBus final EventBus xEventBus) {

		for (final XEventConversion eventConversion : eventConversions) {
			this.conversionMap.put(	eventConversion.getEventCode(),
									eventConversion);
		}
		xEventBus.register(this);
	}

	@Subscribe
	public void handleXEvent(final xcb_generic_event_t event_t) {
		final short responseType = event_t.getResponse_type();

		// TODO handle error cases
		final int eventCode = responseType & 0x7f;

		final XEventConversion eventConversion = this.conversionMap.get(Integer.valueOf(eventCode));
		if (eventConversion == null) {
			return;
		}

		final DisplayEvent displayEvent = eventConversion.convert(event_t);
		if (displayEvent == null) {
			return;
		}

		final AsyncListenable target = eventConversion.getTarget(event_t);
		if (target == null) {
			return;
		}

		target.post(displayEvent);
	}
}
