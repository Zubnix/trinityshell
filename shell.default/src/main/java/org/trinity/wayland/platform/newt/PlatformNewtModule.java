package org.trinity.wayland.platform.newt;

import dagger.Module;
import org.trinity.wayland.protocol.ProtocolModule;

@Module(
        injects = {
                GLWindowFactory.class,
                GLWindowSeatFactory.class
        },
        library = true,
        complete = true,
        includes = {
                ProtocolModule.class
        })
public class PlatformNewtModule {

}
