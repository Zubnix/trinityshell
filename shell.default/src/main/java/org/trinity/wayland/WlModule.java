package org.trinity.wayland;

import com.google.common.util.concurrent.Service;
import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;
import org.trinity.wayland.render.gl.GLModule;

import javax.inject.Singleton;

import static dagger.Provides.Type.SET;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Module(
        includes = {
                GLModule.class
        },
        injects = {
                WlShmRenderer.class,
                WlShellCompositor.class
        },
        library = true
)
public class WlModule {


    @Provides
    @Singleton
    Display provideDisplay() {
        return Display.create();
    }

    @Provides
    @Singleton
    WlScene provideWlScene(){
        return new WlScene();
    }


    @Singleton
    @Provides
    WlJobExecutor provideWlJobExecutor(final Display display){
        return new WlJobExecutor(display);
    }

    @Provides(type = SET)
    Service provideService(final WlShellService wlShellService) {
        return wlShellService;
    }
}
