package org.trinity.x11.defaul.shell;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.shell.xeventhandlers.XEventHandlersModule;

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
	SimpleRootShellSurface provideSimpleRootShellSurface(final XEventLoop xEventLoop,
														 //We have to use the fully qualified classname here or dagger will produce incorrect code.
														 final org.trinity.x11.defaul.XWindowFactory xWindowFactory) {
		return new SimpleRootShellSurface(xWindowFactory.create(xEventLoop.getXcbScreen().getRoot()));
	}

	@Provides
	@Singleton
    SimpleShell provideSimpleShell() {
		return new SimpleShell();
	}
}
