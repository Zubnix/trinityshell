package org.trinity.wayland.defaul.protocol;

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
                WlSeatFactory.class,
                WlDataDeviceFactory.class,
                WlDataDeviceFactory.class,
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

    @Provides
    @Singleton
    WlPointer provideWlPointer(){
        return new WlPointer();
    }

    @Provides
    @Singleton
    WlKeyboard provideWlKeyboard(){
        return new WlKeyboard();
    }

    @Provides
    @Singleton
    WlTouch provideWlTouch(){
        return new WlTouch();
    }
}
