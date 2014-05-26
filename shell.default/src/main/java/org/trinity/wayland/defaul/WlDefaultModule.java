package org.trinity.wayland.defaul;

import com.google.common.util.concurrent.Service;
import dagger.Module;
import dagger.Provides;
import org.trinity.wayland.defaul.protocol.WlProtocolModule;

import static dagger.Provides.Type.SET;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Module(
        includes = {
                WlProtocolModule.class
        },
        injects = {
                WlEventHandler.class
        }
)
public class WlDefaultModule {
    @Provides(type = SET)
    Service provideService(final WlShellService wlShellService) {
        return wlShellService;
    }
}
