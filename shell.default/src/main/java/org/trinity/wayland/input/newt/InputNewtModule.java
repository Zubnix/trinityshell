package org.trinity.wayland.input.newt;

import dagger.Module;

import org.trinity.wayland.input.InputModule;

@Module(injects = GLWindowSeatFactory.class,
        includes = {
                InputModule.class
        },
        library = true,
        complete = true)
public class InputNewtModule {

}
