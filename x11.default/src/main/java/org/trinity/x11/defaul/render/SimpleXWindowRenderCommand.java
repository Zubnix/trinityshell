package org.trinity.x11.defaul.render;

import com.google.auto.factory.AutoFactory;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.defaul.XWindow;
import org.trinity.x11.defaul.XWindowRenderCommand;

import javax.media.nativewindow.util.PointImmutable;

@AutoFactory
public class SimpleXWindowRenderCommand implements XWindowRenderCommand {

    private final ShellSurface shellSurface;

    SimpleXWindowRenderCommand(final ShellSurface shellSurface) {
        this.shellSurface = shellSurface;
    }

    @Override
    public void visit(final XWindow buffer) {
        final PointImmutable position = this.shellSurface.getPosition();
        buffer.move(position.getX(),position.getY());
        buffer.map();
    }
}
