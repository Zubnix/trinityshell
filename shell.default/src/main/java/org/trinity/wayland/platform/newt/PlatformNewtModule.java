package org.trinity.wayland.platform.newt;

import org.trinity.wayland.protocol.WlProtocolModule;

import dagger.Module;

@Module(injects = GLWindowFactory.class,
        library = true,
        complete = false)
public class PlatformNewtModule {

}
