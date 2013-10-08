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
package org.trinity.foundation.display.x11.impl.event;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.XWindowPoolImpl;

import javax.annotation.concurrent.Immutable;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcbConstants.XCB_CONFIGURE_REQUEST;
import static org.freedesktop.xcb.xcb_config_window_t.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.*;

@Bind(multiple = true)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@Immutable
public class ConfigureRequestHandler implements XEventHandler {

    private static final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
            | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
    private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
            .putInt(CLIENT_EVENT_MASK);
    private static final Logger LOG = LoggerFactory.getLogger(ConfigureRequestHandler.class);
    private static final Integer EVENT_CODE = XCB_CONFIGURE_REQUEST;
    private final XConnection xConnection;
    private final XWindowPoolImpl xWindowCache;
    private final EventBus xEventBus;
    private final Display display;

    @Inject
    ConfigureRequestHandler(@XEventBus final EventBus xEventBus,
                            final XConnection xConnection,
                            final XWindowPoolImpl xWindowCache,
                            final Display display) {
        this.xEventBus = xEventBus;
        this.xConnection = xConnection;
        this.xWindowCache = xWindowCache;
        this.display = display;
    }

    @Override
    public Optional<GeometryRequest> handle(final xcb_generic_event_t event_t) {
        final xcb_configure_request_event_t request_event = cast(event_t);

        LOG.debug("Received X event={}",
                request_event.getClass().getSimpleName());

        this.xEventBus.post(request_event);

        final int x = request_event.getX();
        final int y = request_event.getY();
        final int width = request_event.getWidth() + (2 * request_event.getBorder_width());
        final int height = request_event.getHeight() + (2 * request_event.getBorder_width());
        final Rectangle geometry = new ImmutableRectangle(x,
                y,
                width,
                height);

        final int valueMask = request_event.getValue_mask();

        final boolean configureX = (valueMask & XCB_CONFIG_WINDOW_X) != 0;
        final boolean configureY = (valueMask & XCB_CONFIG_WINDOW_Y) != 0;
        final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
        final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

        if (!configureX && !configureY && !configureWidth && !configureHeight) {
            //nothing to configure, so don't send out an event.
            return Optional.absent();
        }

        return Optional.of(new GeometryRequest(geometry,
                configureX,
                configureY,
                configureWidth,
                configureHeight));
    }

    private xcb_configure_request_event_t cast(final xcb_generic_event_t event_t) {
        return new xcb_configure_request_event_t(xcb_generic_event_t.getCPtr(event_t),
                false);
    }

    @Override
    public Optional<DisplaySurface> getTarget(final xcb_generic_event_t event_t) {
        final xcb_configure_request_event_t request_event_t = cast(event_t);
        final int windowId = request_event_t.getWindow();
        final XWindowHandle xWindowHandle = new XWindowHandle(windowId);
        final boolean present = this.xWindowCache.isPresent(xWindowHandle);
        final DisplaySurface displayEventTarget = this.xWindowCache.getDisplaySurface(xWindowHandle);
        if (!present) {
            configureClientEvents(displayEventTarget);
            // this is a bit of a dirty hack to work around X's model of client
            // discovery.
            final DisplaySurfaceCreationNotify displaySurfaceCreationNotify = new DisplaySurfaceCreationNotify(displayEventTarget);
            this.display.post(displaySurfaceCreationNotify);
        }

        return Optional.of(displayEventTarget);
    }

    private void configureClientEvents(final DisplaySurface window) {
        final int winId = (Integer) window.getDisplaySurfaceHandle().getNativeHandle();

        LOG.debug("[winId={}] configure client evens.",
                winId);

        xcb_change_window_attributes(this.xConnection.getConnectionReference().get(),
                winId,
                XCB_CW_EVENT_MASK,
                CLIENT_EVENTS_CONFIG_BUFFER);
        xcb_flush(this.xConnection.getConnectionReference().get());
    }

    @Override
    public Integer getEventCode() {

        return this.EVENT_CODE;
    }
}
