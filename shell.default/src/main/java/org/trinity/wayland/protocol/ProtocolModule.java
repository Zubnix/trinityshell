package org.trinity.wayland.protocol;

import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;
import org.trinity.wayland.output.OutputModule;

import javax.inject.Singleton;

@Module(
        includes = {
                OutputModule.class
        },
        injects = {
                WlSeatFactory.class,
                WlKeyboardFactory.class,
                WlPointerFactory.class,
                WlTouchFactory.class,
                WlDataDeviceFactory.class,
                WlDataDeviceFactory.class,
                WlSurfaceFactory.class,
                WlRegionFactory.class,
                WlShellSurfaceFactory.class,
                org.trinity.wayland.protocol.WlSubSurfaceFactory.class
        },
        staticInjections = {
            EagerSingletons.class
        },
        library = true,
        //depends on wlmodule that needs a render engine
        complete = false
)
public class ProtocolModule {

    @Provides
    @Singleton
    WlSubCompositor provideWlSubCompositor(final Display display,
                                           final org.trinity.wayland.protocol.WlSubSurfaceFactory wlSubSurfaceFactory) {
        return new WlSubCompositor(display,
                                   wlSubSurfaceFactory);
    }
}
