package org.trinity.x11.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.XEventLoop;
import org.trinity.x11.shell.XScene;

import javax.inject.Singleton;

@Module(
        injects = {
                XWindowHandlerFactory.class
        },
        complete = false,
        library = true
)
public class XRenderModule {
    @Provides
    @Singleton
    XShellCompositor provideXCompositor(final XEventLoop xEventLoop,
                                        final org.trinity.x11.XWindowFactory xWindowFactory,
                                        final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory,
                                        final XScene xScene,
                                        final XSimpleRenderer xWindowRenderer,
                                        final org.trinity.x11.render.XWindowHandlerFactory xWindowHandlerFactory) {
        return new XShellCompositor(xEventLoop,
                                    xWindowFactory,
                                    simpleShellSurfaceFactory,
                                    xScene,
                                    xWindowRenderer,
                                    xWindowHandlerFactory);
    }
}
