package org.trinity.foundation.display.x11.impl.render;


import com.google.common.eventbus.Subscribe;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;

import javax.annotation.Nonnull;

public class XEventLinker {
    @Nonnull
    private final Listenable listenable;
    @Nonnull
    private final ShellSurface shellSurface;

    XEventLinker(@Nonnull final Listenable listenable,
                 @Nonnull final ShellSurface shellSurface){
        this.listenable = listenable;
        this.shellSurface = shellSurface;
    }

    public void link(){
        this.listenable.register(this);
    }

    public void unlink(){
        this.listenable.unregister(this);
    }

    @Subscribe
    public void handle(xcb_configure_request_event_t request_event){
		final int x = request_event.getX();
		final int y = request_event.getY();
		final int width = request_event.getWidth() + (2 * request_event.getBorder_width());
		final int height = request_event.getHeight() + (2 * request_event.getBorder_width());

		final int valueMask = request_event.getValue_mask();

		final boolean configureX = (valueMask & XCB_CONFIG_WINDOW_X) != 0;
		final boolean configureY = (valueMask & XCB_CONFIG_WINDOW_Y) != 0;
		final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
		final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

        if(configureX || configureY){
            this.shellSurface.requestMove(x,
                                          y);
        }

        if(configureWidth || configureHeight){
            this.shellSurface.requestResize(width,
                                            height);
        }
    }

    @Subscribe
    public void handle(xcb_map_request_event_t map_request_event){
        this.shellSurface.requestShow();
    }

    @Subscribe
    public void handle(final xcb_unmap_notify_event_t unmap_notify_event){
        this.shellSurface.accept(new ShellSurfaceConfiguration() {
            @Override
            public void configure(ShellSurfaceConfigurable shellSurfaceConfigurable) {
                shellSurfaceConfigurable.setVisible(Boolean.FALSE);
            }
        });
    }

    @Subscribe
    public void handle(final xcb_destroy_notify_event_t destroy_notify_event){
        this.shellSurface.accept(new ShellSurfaceConfiguration() {
            @Override
            public void configure(ShellSurfaceConfigurable shellSurfaceConfigurable) {
                shellSurfaceConfigurable.markDestroyed();
            }
        });
        unlink();
    }
}
