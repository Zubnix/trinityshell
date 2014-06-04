package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.shell.XSimpleShell;

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
    XCompositor provideXCompositor(final XEventLoop                            xEventLoop,
                                   final org.trinity.x11.defaul.XWindowFactory xWindowFactory,
                                   final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory,
                                   final XSimpleShell                          xSimpleShell,
                                   final XSimpleRenderer                       xWindowRenderer,
                                   final XWindowHandlerFactory                 xWindowHandlerFactory) {
		return new XCompositor(xEventLoop,
                               xWindowFactory,
                               simpleShellSurfaceFactory,
                               xSimpleShell,
                               xWindowRenderer,
                               xWindowHandlerFactory);
	}
}
