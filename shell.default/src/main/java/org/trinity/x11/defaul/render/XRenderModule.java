package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.shell.XScene;

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
    XShellCompositor provideXCompositor(final XEventLoop                            xEventLoop,
                                   final org.trinity.x11.defaul.XWindowFactory xWindowFactory,
                                   final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory,
                                   final XScene xScene,
                                   final XSimpleRenderer                       xWindowRenderer,
                                   final XWindowHandlerFactory                 xWindowHandlerFactory) {
		return new XShellCompositor(xEventLoop,
                               xWindowFactory,
                               simpleShellSurfaceFactory,
                               xScene,
                               xWindowRenderer,
                               xWindowHandlerFactory);
	}
}
