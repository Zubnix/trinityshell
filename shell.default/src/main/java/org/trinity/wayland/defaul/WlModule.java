package org.trinity.wayland.defaul;

import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;

import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Module(
        injects = EagerSingletons.class
)
public class WlModule {

    @Provides
    @Singleton
    WlShell provideWlShell(final Display display) {
        return new WlShell(display);
    }

    @Provides
    @Singleton
    WlCompositor provideWlCompositor(final Display display,
                                     final WlShm wlShm,
                                     final Renderer renderer) {
        final WlCompositor wlCompositor = new WlCompositor(display,
                wlShm,
                renderer);

        return wlCompositor;
    }

    @Provides
    @Singleton
    WlShm provideWlShm(final Display display) {
        return new WlShm(display);
    }

    @Provides
    @Singleton
    Display provideDisplay() {
        return new Display();
    }
}
