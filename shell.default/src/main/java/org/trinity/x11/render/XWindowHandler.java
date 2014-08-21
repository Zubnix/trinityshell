package org.trinity.x11.render;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.event.Destroyed;
import org.trinity.shell.scene.api.event.ResizeRequest;
import org.trinity.shell.scene.api.event.ShowRequest;
import org.trinity.x11.XWindow;

import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Rectangle;

@AutoFactory(className = "XWindowHandlerFactory")
public class XWindowHandler {

    private final XWindow xWindow;

    XWindowHandler(final XWindow xWindow) {
        this.xWindow = xWindow;
    }

    @Subscribe
    public void handle(final ResizeRequest resizeRequest) {

        final DimensionImmutable size   = resizeRequest.getSize();
        final int width                 = size.getWidth();
        final int height                = size.getHeight();
        final XWindow buffer            = this.xWindow.resize(width,
                                                              height);
        resizeRequest.getSource()
                     .accept(shellSurfaceConfigurable ->
                             shellSurfaceConfigurable.attachBuffer(buffer,
                                                                   0,
                                                                   0)
                                                     .markDamaged(new Rectangle(0,
                                                                                0,
                                                                                width,
                                                                                height))
                                                     .commit());
    }

    @Subscribe
    public void handle(final ShowRequest showRequest) {

        final DimensionImmutable size = this.xWindow.getSize();
        final int width               = size.getWidth();
        final int height              = size.getHeight();

        showRequest.getSource()
                   .accept(shellSurfaceConfigurable ->
                           shellSurfaceConfigurable.attachBuffer(XWindowHandler.this.xWindow,
                                                                 0,
                                                                 0)
                                                   .markDamaged(new Rectangle(0,
                                                                              0,
                                                                              width,
                                                                              height))
                                                   .commit());
    }

    @Subscribe
    public void handle(final Destroyed destroyed) {
        destroyed.getSource()
                 .unregister(this);
    }
}
