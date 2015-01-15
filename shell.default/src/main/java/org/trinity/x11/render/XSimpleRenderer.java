package org.trinity.x11.render;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.XWindow;
import org.trinity.x11.XWindowRenderer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;

public class XSimpleRenderer implements XWindowRenderer {

    private final EventBus dispatcher = new EventBus();
    private ShellSurface currentShellSurface;

    @Inject
    XSimpleRenderer() {
        this.dispatcher.register(this);
    }

    private void setCurrentShellSurface(final ShellSurface currentShellSurface) {
        this.currentShellSurface = currentShellSurface;
    }

    private ShellSurface getCurrentShellSurface() {
        return this.currentShellSurface;
    }

    public void render(@Nonnull final ShellSurface shellSurface) {
        setCurrentShellSurface(shellSurface);
        final Optional<?> optionalBuffer = shellSurface.getBuffer();
        if (optionalBuffer.isPresent()) {
            this.dispatcher.post(optionalBuffer.get());
        }
    }

    @Subscribe
    public void render(final XWindow buffer) {
        final PointImmutable position = getCurrentShellSurface().getPosition();
        buffer.move(position.getX(),
                    position.getY());
        buffer.map();
    }
}