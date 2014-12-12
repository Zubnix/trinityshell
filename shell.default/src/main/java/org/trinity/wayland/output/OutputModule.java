package org.trinity.wayland.output;

import com.google.common.util.concurrent.Service;
import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;

import javax.inject.Singleton;

import static dagger.Provides.Type.SET;

@Module(injects = {
                ShmRendererFactory.class,
                CompositorFactory.class
        },
        library = true,
        //needs render engine implementation, defined at startup.
        complete = false
)
public class OutputModule {


    @Provides
    @Singleton
    Display provideDisplay() {
        return Display.create();
    }

    @Provides
    @Singleton
    Scene provideWlScene(){
        return new Scene();
    }


    @Singleton
    @Provides
    JobExecutor provideWlJobExecutor(final Display display){
        return new JobExecutor(display);
    }

    @Provides(type = SET)
    Service provideService(final ShellService shellService) {
        return shellService;
    }
}
