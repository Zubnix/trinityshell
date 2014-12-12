package org.trinity.wayland.input;

import org.trinity.wayland.protocol.ProtocolModule;

import dagger.Module;

@Module(
    includes = {
        ProtocolModule.class
    },
    injects = {
        Seat.class
    }
)
public class InputModule {

}
