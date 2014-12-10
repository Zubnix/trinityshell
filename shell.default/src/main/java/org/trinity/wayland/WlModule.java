package org.trinity.wayland;

import com.google.common.util.concurrent.Service;
import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;
import org.trinity.wayland.render.gl.GLModule;

import javax.inject.Singleton;

import static dagger.Provides.Type.SET;

@Module(injects = {
                WlShmRendererFactory.class,
                WlShellCompositorFactory.class
        },
        library = true,

        //needs render engine implementation
        complete = false
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
