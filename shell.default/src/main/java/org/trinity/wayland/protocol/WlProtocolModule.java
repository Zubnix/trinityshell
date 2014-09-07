package org.trinity.wayland.protocol;

import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;
import org.trinity.wayland.WlModule;

import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Module(
        includes = {
                WlModule.class
        },
        staticInjections = {
          EagerSingletons.class
        },
        injects = {
                WlSeatFactory.class,
                WlDataDeviceFactory.class,
                WlDataDeviceFactory.class,
                WlSurfaceFactory.class,
                WlRegionFactory.class,
                WlShellSurfaceFactory.class,
                org.trinity.wayland.protocol.WlSubSurfaceFactory.class
        },
        library = true
)
public class WlProtocolModule {

    @Provides
    @Singleton
    WlSubCompositor provideWlSubCompositor(final Display             display,
                                           final org.trinity.wayland.protocol.WlSubSurfaceFactory wlSubSurfaceFactory){
        return new WlSubCompositor(display,
                                   wlSubSurfaceFactory);
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
