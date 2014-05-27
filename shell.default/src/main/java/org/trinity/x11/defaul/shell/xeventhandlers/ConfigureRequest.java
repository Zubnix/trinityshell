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
package org.trinity.x11.defaul.shell.xeventhandlers;

import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.defaul.XEventHandler;
import org.trinity.x11.defaul.XSurfacePool;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CONFIGURE_REQUEST;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_X;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_Y;

@Immutable
public class ConfigureRequest implements XEventHandler {

    private static final Logger  LOG        = LoggerFactory.getLogger(ConfigureRequest.class);
    private static final Integer EVENT_CODE = XCB_CONFIGURE_REQUEST;

    private final XSurfacePool xSurfacePool;

    @Inject
    ConfigureRequest(final XSurfacePool xSurfacePool) {
        this.xSurfacePool = xSurfacePool;
    }

    @Override
    public void handle(@Nonnull final xcb_generic_event_t event) {
        final xcb_configure_request_event_t request_event = cast(event);
        LOG.debug("Received X event={}",
                  request_event.getClass()
                               .getSimpleName()
                 );

        final int value_mask            = request_event.getValue_mask();

        final boolean configureX        = (value_mask & XCB_CONFIG_WINDOW_X) != 0;
        final boolean configureY        = (value_mask & XCB_CONFIG_WINDOW_Y) != 0;
        //we implicitly require clients to provide both x and y values here,
        //a more advanced implementation can easily work around this restriction.
        final boolean positionRequest   = configureX && configureY;

        final boolean configureWidth    = (value_mask & XCB_CONFIG_WINDOW_WIDTH) != 0;
        final boolean configureHeight   = (value_mask & XCB_CONFIG_WINDOW_HEIGHT) != 0;
        //we implicitly require clients to provide both width and height values here,
        //a more advanced implementation can easily work around this restriction.
        final boolean sizeRequest       = configureWidth && configureHeight;

        final ShellSurface shellSurface = this.xSurfacePool.get(request_event.getWindow());

        if(positionRequest) {
            shellSurface.requestMove(request_event.getX(),
                                     request_event.getY());
        }

        if(sizeRequest) {
            shellSurface.requestResize(request_event.getWidth(),
                                       request_event.getHeight());
        }
    }

    private xcb_configure_request_event_t cast(final xcb_generic_event_t event_t) {
        return new xcb_configure_request_event_t(xcb_generic_event_t.getCPtr(event_t),
                                                 false);
    }

    @Override
    public Integer getEventCode() {
        return EVENT_CODE;
    }
}
