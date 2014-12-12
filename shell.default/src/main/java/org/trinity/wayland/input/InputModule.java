package org.trinity.wayland.input;

import dagger.Module;
import org.trinity.wayland.protocol.ProtocolModule;

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
