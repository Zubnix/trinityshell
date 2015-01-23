package org.trinity.bootstrap;

import dagger.Module;
import org.trinity.wayland.output.gl.OutputGLModule;
import org.trinity.wayland.platform.newt.PlatformNewtModule;
import org.trinity.wayland.protocol.ProtocolModule;

@Module(
        injects = {
                EntryPoint.class
        },
        includes = {
                OutputGLModule.class,
                ProtocolModule.class,
                PlatformNewtModule.class,
        },
        complete = true,
        library = false
)
public class TrinityShellModule {

}
