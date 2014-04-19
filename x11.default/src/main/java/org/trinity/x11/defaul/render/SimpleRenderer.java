package org.trinity.x11.defaul.render;

import org.trinity.shell.scene.api.Buffer;
import org.trinity.shell.scene.api.ShellSurface;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class SimpleRenderer {

    @Nonnull
    private final SimpleXWindowRenderCommandFactory xWindowRenderCommandFactory;

    @Inject
    SimpleRenderer(@Nonnull final SimpleXWindowRenderCommandFactory xWindowRenderCommandFactory) {
        this.xWindowRenderCommandFactory = xWindowRenderCommandFactory;
    }

    public void render(@Nonnull final ShellSurface shellSurface) {
        final Optional<Buffer> optionalBuffer = shellSurface.getBuffer();
        if(optionalBuffer.isPresent()){
            optionalBuffer.get().accept(this.xWindowRenderCommandFactory.create(shellSurface));
        }
    }
}