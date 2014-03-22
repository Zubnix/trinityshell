package org.trinity.x11.defaul.shell;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.freedesktop.xcb.xcb_unmap_notify_event_t;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.event.Destroyed;
import org.trinity.shell.scene.api.event.MoveRequest;
import org.trinity.shell.scene.api.event.ResizeRequest;
import org.trinity.shell.scene.api.event.ShowRequest;

import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.PointImmutable;

import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;


public class SimpleShellSurfaceBehavior {

	private SimpleShellSurface simpleShellSurface;

	public void visit(final SimpleShellSurface simpleShellSurface) {
		this.simpleShellSurface = simpleShellSurface;
	}

	@Subscribe
	public void handle(final xcb_configure_request_event_t request_event) {
		//We ignore any position requests from the client. We're the compositor, we're in charge.
        final int valueMask = request_event.getValue_mask();
        final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
        final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

        if (configureWidth || configureHeight) {
            //we mimic client side buffer control (as is the case in wayland) by configuring the window size as-is.
            final int width = request_event.getWidth();
            final int height = request_event.getHeight();
			this.simpleShellSurface.getBuffer().configure(width,
														  height);
        }
    }

    @Subscribe
    public void handle(final xcb_map_request_event_t map_request_event) {
		this.simpleShellSurface.requestShow();
	}

    @Subscribe
    public void handle(final xcb_unmap_notify_event_t unmap_notify_event) {
		this.simpleShellSurface.accept(shellSurfaceConfigurable ->
											   shellSurfaceConfigurable
													   .setVisible(Boolean.FALSE));
	}

    @Subscribe
    public void handle(final xcb_destroy_notify_event_t destroy_notify_event) {
		this.simpleShellSurface.accept(ShellSurfaceConfigurable::markDestroyed);
    }

    @Subscribe
    public void handle(final MoveRequest moveRequest) {
        final PointImmutable position = moveRequest.getPosition();
        final ShellSurface shellSurface = moveRequest.getSource();

		shellSurface.accept(shellSurfaceConfigurable ->
									shellSurfaceConfigurable
											.setPosition(position)
											.commit());
	}

	@Subscribe
	public void handle(final ResizeRequest resizeRequest){
		final DimensionImmutable size = resizeRequest.getSize();
		this.simpleShellSurface.getBuffer().configure(size.getWidth(),
							   						  size.getHeight());
	}

    @Subscribe
    public void handle(final ShowRequest showRequest) {
		this.simpleShellSurface.accept(shellSurfaceConfigurable ->
											   shellSurfaceConfigurable
													   .setVisible(Boolean.TRUE)
													   .commit());
	}

	@Subscribe
	public void handle(final Destroyed destroyed){
		this.simpleShellSurface.unregister(this);
		this.simpleShellSurface.getBuffer().unregister(this);
	}
}