package org.trinity.x11.defaul.render;

import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.ShellSurfaceMoveRequest;
import org.trinity.shell.scene.api.event.ShellSurfaceShowRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.PointImmutable;

@Singleton
public class SimpleRenderer {

    @Inject
	SimpleRenderer() {
    }

    public SimpleRenderer add(@Nonnull final ShellSurface shellSurface) {
        registerSurfaceListeners(shellSurface);
        return this;
    }

    private void registerSurfaceListeners(final ShellSurface shellSurface) {
        shellSurface.register(this);
    }

    @Subscribe
    public void handle(final ShellSurfaceMoveRequest shellSurfaceMoveRequest){

        final PointImmutable position = shellSurfaceMoveRequest.getPosition();
        final ShellSurface shellSurface = shellSurfaceMoveRequest.getSource();

		shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setPosition(position));
	}

	@Subscribe
	public void handle(final ShellSurfaceShowRequest shellSurfaceShowRequest) {

        final ShellSurface shellSurface = shellSurfaceShowRequest.getSource();

		shellSurface.accept(shellSurfaceConfigurable -> shellSurfaceConfigurable.setVisible(Boolean.TRUE));

        //TODO how to make window visible?
	}

	//TODO more business logic for shell layout & behavior here
}
