package org.trinity.wayland.input.newt;

import org.trinity.wayland.protocol.WlProtocolModule;

import dagger.Module;

@Module(injects = GLWindowSeatFactory.class,
        includes = {
                WlProtocolModule.class
        },
        library = true,
        complete = true)
public class InputNewtModule {

}
