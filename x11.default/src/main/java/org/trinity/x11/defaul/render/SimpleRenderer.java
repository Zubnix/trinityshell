package org.trinity.x11.defaul.render;

import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.x11.defaul.XWindow;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SimpleRenderer {

    @Inject
    SimpleRenderer() {
    }

    public SimpleRenderer add(@Nonnull final XWindow xWindow,
                              @Nonnull final ShellSurface shellSurface) {
        new SimpleRenderHandler(xWindow,
                                 shellSurface).handle();
        return this;
    }
}
