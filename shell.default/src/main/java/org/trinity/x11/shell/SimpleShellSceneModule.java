package org.trinity.x11.shell;

import dagger.Module;
import dagger.Provides;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.x11.shell.xeventhandlers.XEventHandlersModule;

import javax.inject.Singleton;

@Module(
        includes = {
                XEventHandlersModule.class
        },
        injects = {
                SimpleShellSurfaceFactory.class
        },
        complete = true,
        library = true
)
public class SimpleShellSceneModule {

    @Provides
    @Singleton
    XScene provideSimpleShell() {
        return new XScene();
    }
}
