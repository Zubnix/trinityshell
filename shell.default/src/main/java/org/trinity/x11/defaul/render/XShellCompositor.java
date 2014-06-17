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
import org.trinity.x11.defaul.shell.XScene;

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
public class XShellCompositor {

    private static final int        CLIENT_EVENT_MASK           = XCB_EVENT_MASK_ENTER_WINDOW
                                                                | XCB_EVENT_MASK_LEAVE_WINDOW
                                                                | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
    private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
                                                                                   .putInt(CLIENT_EVENT_MASK);
    private final XEventLoop                xEventLoop;
    private final XWindowFactory            xWindowFactory;
    private final SimpleShellSurfaceFactory simpleShellSurfaceFactory;
    private final XScene                    xScene;
    private final XSimpleRenderer           xWindowRenderer;
    private final XWindowHandlerFactory     xWindowHandlerFactory;

    @Inject
    XShellCompositor(final XEventLoop xEventLoop,
                     final XWindowFactory xWindowFactory,
                     final SimpleShellSurfaceFactory simpleShellSurfaceFactory,
                     final XScene xScene,
                     final XSimpleRenderer xWindowRenderer,
                     final XWindowHandlerFactory xWindowHandlerFactory) {

        this.xEventLoop                = xEventLoop;
        this.xWindowFactory            = xWindowFactory;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
        this.xScene                    = xScene;
        this.xWindowRenderer           = xWindowRenderer;
        this.xWindowHandlerFactory     = xWindowHandlerFactory;
    }

    @Nonnull
    public ShellSurface create(@Nonnull final Integer windowHandle) {
        configure(windowHandle);

        final XWindow xWindow               = this.xWindowFactory.create(windowHandle);
        final XWindowHandler xWindowHandler = this.xWindowHandlerFactory.create(xWindow);
        final ShellSurface shellSurface     = this.simpleShellSurfaceFactory.create(Optional.of(xWindow));

        shellSurface.register(this);
        shellSurface.register(xWindowHandler);

        this.xScene.add(shellSurface);

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
        destroyed.getSource()
                 .unregister(this);
    }

    private void requestRender(final ShellSurface shellSurface) {
        if(this.xScene.needsRedraw(shellSurface)) {
            this.xScene.getShellSurfacesStack()
                       .forEach(this.xWindowRenderer::render);
        }
    }

    private void configure(final Integer windowHandle) {
        xcb_change_window_attributes(this.xEventLoop.getXcbConnection(),
                                     windowHandle,
                                     XCB_CW_EVENT_MASK,
                                     CLIENT_EVENTS_CONFIG_BUFFER);
        xcb_flush(this.xEventLoop.getXcbConnection());
    }
}