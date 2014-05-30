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
package org.trinity.x11.defaul.render;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;
import org.trinity.shell.scene.api.event.Destroyed;
import org.trinity.shell.scene.api.event.Hidden;
import org.trinity.shell.scene.api.event.Lowered;
import org.trinity.shell.scene.api.event.Moved;
import org.trinity.shell.scene.api.event.Raised;
import org.trinity.shell.scene.api.event.Showed;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.XWindow;
import org.trinity.x11.defaul.XWindowFactory;
import org.trinity.x11.defaul.shell.XSimpleShell;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.Optional;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY;

@Singleton
public class XCompositor implements org.trinity.x11.defaul.XCompositor {

    private static final Logger LOG = LoggerFactory.getLogger(XCompositor.class);

    private static final int        CLIENT_EVENT_MASK           = XCB_EVENT_MASK_ENTER_WINDOW
                                                                | XCB_EVENT_MASK_LEAVE_WINDOW
                                                                | XCB_EVENT_MASK_STRUCTURE_NOTIFY;

    private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
                                                                                   .putInt(CLIENT_EVENT_MASK);
    private final XEventLoop     xEventLoop;
    private final XWindowFactory xWindowFactory;

    private final SimpleShellSurfaceFactory     simpleShellSurfaceFactory;
    private final XSimpleShell                  XSimpleShell;
    private final XSimpleRenderer               XWindowRenderer;
    private final XBufferHandlerFactory         xBufferHandlerFactory;

    @Inject
    XCompositor(final XEventLoop                    xEventLoop,
                final XWindowFactory                xWindowFactory,
                final SimpleShellSurfaceFactory     simpleShellSurfaceFactory,
                final XSimpleShell                  xSimpleShell,
                final XSimpleRenderer               xWindowRenderer,
                final XBufferHandlerFactory         xBufferHandlerFactory) {

        this.xEventLoop     = xEventLoop;
        this.xWindowFactory = xWindowFactory;

        this.simpleShellSurfaceFactory      = simpleShellSurfaceFactory;
        this.XSimpleShell                   = xSimpleShell;
        this.XWindowRenderer                = xWindowRenderer;
        this.xBufferHandlerFactory          = xBufferHandlerFactory;
    }

    @Nonnull
    @Override
    public ShellSurface create(@Nonnull final Integer nativeHandle) {
        configureClientEvents(nativeHandle);

        final XWindow xWindow               = this.xWindowFactory.create(nativeHandle);
        final XBufferHandler XBufferHandler = this.xBufferHandlerFactory.create(xWindow);
        final ShellSurface shellSurface     = this.simpleShellSurfaceFactory.create(Optional.of(xWindow));

        shellSurface.register(this);
        shellSurface.register(XBufferHandler);

        this.XSimpleShell.add(shellSurface);

        return shellSurface;
    }

    @Subscribe
    public void handle(final Committed committed) {

        final ShellSurface shellSurface = committed.getSource();

        if(shellSurface.getDamage()
                       .isPresent()) {
            requestRender(shellSurface);
        }
    }

    @Subscribe
    public void handle(final Raised raised) {
        requestRender(raised.getSource());
    }

    @Subscribe
    public void handle(final Lowered lowered) {
        requestRender(lowered.getSource());
    }

    @Subscribe
    public void handle(final Showed showed) {
        requestRender(showed.getSource());
    }

    @Subscribe
    public void handle(final Hidden hidden) {
        requestRender(hidden.getSource());
    }

    @Subscribe
    public void handle(final Moved moved) {
        requestRender(moved.getSource());
    }

    @Subscribe
    public void handle(final Destroyed destroyed) {
        destroyed.getSource().unregister(this);
    }

    private void requestRender(final ShellSurface shellSurface) {

        if(this.XSimpleShell.needsRedraw(shellSurface)) {
            this.XSimpleShell.getShellSurfacesStack().forEach(this.XWindowRenderer::render);
        }
    }

    private void configureClientEvents(final Integer nativeHandle) {

        LOG.debug("[winId={}] visit client evens.",
                  nativeHandle);

        xcb_change_window_attributes(this.xEventLoop.getXcbConnection(),
                                     nativeHandle,
                                     XCB_CW_EVENT_MASK,
                                     CLIENT_EVENTS_CONFIG_BUFFER);
        xcb_flush(this.xEventLoop.getXcbConnection());
    }
}