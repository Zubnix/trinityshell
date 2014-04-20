package org.trinity.x11.defaul.render;

import org.trinity.shell.scene.api.Buffer;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.defaul.XWindow;
import org.trinity.x11.defaul.XWindowRenderer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;

public class SimpleRenderer implements XWindowRenderer {

    private ShellSurface currentShellSurface;

    @Inject
    SimpleRenderer() {
    }

    private void setCurrentShellSurface(final ShellSurface currentShellSurface) {
        this.currentShellSurface = currentShellSurface;
    }

    private ShellSurface getCurrentShellSurface() {
        return this.currentShellSurface;
    }

    public void render(@Nonnull final ShellSurface shellSurface) {
        setCurrentShellSurface(shellSurface);
        final Optional<Buffer> optionalBuffer = shellSurface.getBuffer();
        if(optionalBuffer.isPresent()){
            optionalBuffer.get()
                          .accept(this);
        }
    }

    @Override
    public void visit(final XWindow buffer) {
        final PointImmutable position = getCurrentShellSurface().getPosition();
        buffer.move(position.getX(),
                    position.getY());
        buffer.map();
    }
}