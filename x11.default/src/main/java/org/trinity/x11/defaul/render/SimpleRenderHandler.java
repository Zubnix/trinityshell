package org.trinity.x11.defaul.render;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_configure_notify_event_t;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_map_notify_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.freedesktop.xcb.xcb_unmap_notify_event_t;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.event.MoveRequest;
import org.trinity.shell.scene.api.event.ShowRequest;
import org.trinity.x11.defaul.XWindow;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;

import static org.freedesktop.xcb.xcb_config_window_t.*;

public class SimpleRenderHandler {
    @Nonnull
    private final XWindow xWindow;
    @Nonnull
    private final ShellSurface shellSurface;

    public SimpleRenderHandler(@Nonnull final XWindow xWindow,
                               @Nonnull final ShellSurface shellSurface) {
        this.xWindow = xWindow;
        this.shellSurface = shellSurface;
    }

    @Subscribe
    public void handle(final xcb_configure_request_event_t request_event) {

        final int valueMask = request_event.getValue_mask();

        final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
        final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

        if (configureWidth || configureHeight) {
            //we mimic client side buffer control (as is the case in wayland) by configuring the window size as-is.
            final int width = request_event.getWidth();
            final int height = request_event.getHeight();
            this.xWindow.configure(width,
                                   height);
        }
    }

    @Subscribe
    public void handle(final xcb_configure_notify_event_t configure_notify_event) {
        //signal the compositor to schedule a repaint.
        this.shellSurface.accept((shellSurfaceConfigurable) ->  shellSurfaceConfigurable.attachBuffer(this.xWindow).commit());
    }

    @Subscribe
    public void handle(final xcb_map_request_event_t map_request_event) {
        this.shellSurface.requestShow();
    }

    @Subscribe
    public void handle(final xcb_map_notify_event_t map_notify_event){
        this.shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setVisible(Boolean.TRUE).commit());
    }

    @Subscribe
    public void handle(final xcb_unmap_notify_event_t unmap_notify_event) {
        this.shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setVisible(Boolean.FALSE).commit());
    }

    @Subscribe
    public void handle(final xcb_destroy_notify_event_t destroy_notify_event) {
        this.shellSurface.accept(ShellSurfaceConfigurable::markDestroyed);
        this.xWindow.unregister(this);
    }

    @Subscribe
    public void handle(final MoveRequest moveRequest) {

        final PointImmutable position = moveRequest.getPosition();
        final ShellSurface shellSurface = moveRequest.getSource();

        shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setPosition(position).commit());
    }

    @Subscribe
    public void handle(final ShowRequest showRequest) {
        this.xWindow.map();
    }
}
