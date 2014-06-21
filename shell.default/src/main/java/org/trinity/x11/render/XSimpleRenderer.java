package org.trinity.x11.render;

import org.trinity.shell.scene.api.Buffer;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.XWindow;
import org.trinity.x11.XWindowRenderer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;

public class XSimpleRenderer implements XWindowRenderer {

    private ShellSurface currentShellSurface;

    @Inject
    XSimpleRenderer() {
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