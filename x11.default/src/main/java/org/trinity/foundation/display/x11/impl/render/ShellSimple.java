package org.trinity.foundation.display.x11.impl.render;

import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.shell.scene.api.event.ShellSurfaceHideRequestEvent;
import org.trinity.shell.scene.api.event.ShellSurfaceShowRequestEvent;
import org.trinity.shell.scene.api.event.ShellSurfaceVisibleRequestEvent;
import org.trinity.shell.scene.api.event.ShellSurfaceResizeRequestEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.RectangleImmutable;

@Singleton
public class ShellSimple {

    @Inject
	ShellSimple() {
    }

	public void add(final ShellSurface shellSurface) {
        registerSurfaceListeners(shellSurface);
    }

    private void registerSurfaceListeners(final ShellSurface shellSurface) {
        shellSurface.register(this);
    }

    @Subscribe
    public void handle(ShellSurfaceResizeRequestEvent shellSurfaceResizeRequestEvent){

        final DimensionImmutable size = shellSurfaceResizeRequestEvent.getSize();
        final ShellSurface shellSurface = shellSurfaceResizeRequestEvent.getSource();
        final RectangleImmutable shape = shellSurface.getShape();
        shellSurface.accept(new ShellSurfaceConfiguration() {
            @Override
            public void visit(ShellSurfaceConfigurable shellSurfaceConfigurable) {
                shellSurfaceConfigurable.configure(shape.getX(),
                                                   shape.getY(),
                                                   size.getWidth(),
                                                   size.getHeight());
            }
        });
    }

    @Subscribe
    public void handle(final ShellSurfaceShowRequestEvent shellSurfaceShowRequestEvent){
        final ShellSurface shellSurface = shellSurfaceShowRequestEvent.getSource();
        shellSurface.accept(new ShellSurfaceConfiguration() {
            @Override
            public void visit(ShellSurfaceConfigurable shellSurfaceConfigurable) {
                shellSurfaceConfigurable.setVisible(Boolean.TRUE);
            }
        });
    }

    //TODO more business logic for shell layout & behavior here
}
