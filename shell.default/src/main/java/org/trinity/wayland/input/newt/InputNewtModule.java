package org.trinity.wayland.input.newt;

import dagger.Module;
import org.trinity.wayland.protocol.WlProtocolModule;

@Module(injects = GLWindowSeatFactory.class,
        includes = {
                WlProtocolModule.class
        },
        library = true,
        complete = true)
public class InputNewtModule {

}
