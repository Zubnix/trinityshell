package org.trinity.x11.defaul.shell;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_configure_notify_event_t;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.freedesktop.xcb.xcb_unmap_notify_event_t;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.x11.defaul.XWindow;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;

import static org.freedesktop.xcb.xcb_config_window_t.*;

public class SimpleXEventTranslator {
    @Nonnull
    private final XWindow xWindow;
    @Nonnull
    private final ShellSurface shellSurface;

    public SimpleXEventTranslator(@Nonnull final XWindow xWindow,
                                  @Nonnull final ShellSurface shellSurface) {
        this.xWindow = xWindow;
        this.shellSurface = shellSurface;
    }

    @Nonnull
    public SimpleXEventTranslator register() {
        this.xWindow.register(this);
        return this;
    }

    @Nonnull
    public SimpleXEventTranslator unregister() {
        this.xWindow.unregister(this);
        return this;
    }

    @Subscribe
    public void handle(final xcb_configure_request_event_t request_event) {

        final int valueMask = request_event.getValue_mask();

        final boolean configureX = (valueMask & XCB_CONFIG_WINDOW_X) != 0;
        final boolean configureY = (valueMask & XCB_CONFIG_WINDOW_Y) != 0;
        final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
        final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

        if (configureX || configureY) {
            final int x = request_event.getX();
            final int y = request_event.getY();

            this.shellSurface.requestMove(x,
                                          y);
        }

        if (configureWidth || configureHeight) {
            //we mimic client side buffer control (as is the case in wayland) by configuring the window as-is.
            final int border_width = request_event.getBorder_width();
            final int width = request_event.getWidth()+border_width;
            final int height = request_event.getHeight()+border_width;

            final PointImmutable position = this.shellSurface.getPosition();

            //signal that the the window has changed.
            this.shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.attachBuffer(this.xWindow.configure(position.getX(),
                                                                                                                              position.getY(),
                                                                                                                              width,
                                                                                                                              height)));
        }
    }

    @Subscribe
    public void handle(final xcb_configure_notify_event_t configure_notify_event) {
        //signal the compositor to schedule a repaint.
        this.shellSurface.accept(ShellSurfaceConfigurable::commit);
    }

    @Subscribe
    public void handle(final xcb_map_request_event_t map_request_event) {
        this.shellSurface.requestShow();
    }

    @Subscribe
    public void handle(final xcb_unmap_notify_event_t unmap_notify_event) {
        this.shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setVisible(Boolean.FALSE));
    }

    @Subscribe
    public void handle(final xcb_destroy_notify_event_t destroy_notify_event) {
        this.shellSurface.accept(ShellSurfaceConfigurable::markDestroyed);
        unregister();
    }
}
