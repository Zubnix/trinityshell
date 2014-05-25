package org.trinity.wayland.defaul;

import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;

import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Module(
        staticInjections = {
          EagerSingletons.class
        },
        injects = {
                WlShmPoolFactory.class,
                WlShmBufferFactory.class,
                WlSurfaceFactory.class,
                WlRegionFactory.class,
                WlShellSurfaceFactory.class
        }
)
public class WlModule {

    @Provides
    @Singleton
    Display provideDisplay() {
        return new Display();
    }
}
