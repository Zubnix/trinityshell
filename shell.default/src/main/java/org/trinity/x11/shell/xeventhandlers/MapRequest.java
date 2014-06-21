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
package org.trinity.x11.shell.xeventhandlers;

import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.trinity.x11.XEventHandler;
import org.trinity.x11.XSurfacePool;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_MAP_REQUEST;

@Immutable
public class MapRequest implements XEventHandler {

    private static final Integer EVENT_CODE = XCB_MAP_REQUEST;

    private final XSurfacePool xSurfacePool;

    @Inject
    MapRequest(final XSurfacePool xSurfacePool) {
        this.xSurfacePool = xSurfacePool;
    }

    @Override
    public void handle(@Nonnull final xcb_generic_event_t event) {
        final xcb_map_request_event_t map_request_event = cast(event);
        this.xSurfacePool.get(map_request_event.getWindow())
                         .requestShow();
    }

    private xcb_map_request_event_t cast(final xcb_generic_event_t event) {
        return new xcb_map_request_event_t(xcb_generic_event_t.getCPtr(event),
                                           false);
    }

    @Override
    public Integer getEventCode() {
        return EVENT_CODE;
    }
}
