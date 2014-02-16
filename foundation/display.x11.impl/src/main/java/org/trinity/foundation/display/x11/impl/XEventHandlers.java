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
package org.trinity.foundation.display.x11.impl;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
public final class XEventHandlers {

    private static final Logger LOG = LoggerFactory.getLogger(XEventHandlers.class);
    private static final int EVENT_CODE_MASK = 0x7f;

    /*
     * Singletons are eagerly created in Guice by default. So even though nobody
     * needs an instance, it's still created anyway. In the case of
     * XEventHandlers, when Guice creates it (at binding time at startup), it
     * immediately subscribes itself to the XEventBus and starts processing x
     * events in it's @Subscribe method as soon as events arrive.
     */
    private final Map<Integer, XEventHandler> conversionMap = new HashMap<>();

    @Inject
    XEventHandlers(final Set<XEventHandler> eventConversions,
                   @XEventBus final EventBus xEventBus) {

        for (final XEventHandler eventConversion : eventConversions) {
            this.conversionMap.put(eventConversion.getEventCode(),
                    eventConversion);
        }
        xEventBus.register(this);
    }

    @Subscribe
    public void handleXEvent(final xcb_generic_event_t event) {
        final short response_type = event.getResponse_type();

        // TODO handle error cases
        final int eventCode = response_type & EVENT_CODE_MASK;

        final XEventHandler eventConversion = this.conversionMap.get(Integer.valueOf(eventCode));
        if (eventConversion == null) {
            return;
        }

        final Optional<? extends DisplayEvent> displayEvent = eventConversion.handle(event);
        final Optional<? extends Listenable> target = eventConversion.getTarget(event);

        if (displayEvent.isPresent() && target.isPresent()) {
            target.get().post(displayEvent.get());
        }
    }
}
