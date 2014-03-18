/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.x11.defaul;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_generic_event_t;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
@NotThreadSafe
public final class XEventHandlers {

    private static final int EVENT_CODE_MASK = 0x7f;

    private final Map<Integer, XEventHandler> conversionMap = new HashMap<>();

    @Inject
    XEventHandlers(final Set<XEventHandler> eventConversions) {

        for (final XEventHandler eventConversion : eventConversions) {
            this.conversionMap.put(eventConversion.getEventCode(),
                    eventConversion);
        }
    }

    @Subscribe
    public void handleXEvent(final xcb_generic_event_t event) {
        final short response_type = event.getResponse_type();

        // TODO handle error cases
        final int eventCode = response_type & EVENT_CODE_MASK;

        final XEventHandler eventConversion = this.conversionMap.get(eventCode);
        if (eventConversion == null) {
            return;
        }

        eventConversion.handle(event);
    }
}
