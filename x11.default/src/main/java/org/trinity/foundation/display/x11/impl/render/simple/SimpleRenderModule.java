package org.trinity.foundation.display.x11.impl.render.simple;

import dagger.Module;
import dagger.Provides;
import org.trinity.foundation.display.x11.impl.XCompositor;
import org.trinity.foundation.display.x11.impl.XEventChannel;

import javax.inject.Singleton;

@Module(
		injects = {
			XCompositorSimple.class
		},
		complete = false,
		library = true
)
public class SimpleRenderModule {
	@Provides
	@Singleton
	XCompositor provideXCompositor(final XCompositorSimple xCompositorSimple) {
		return xCompositorSimple;
	}

	@Provides
	@Singleton
	ShellSurfaceRootSimple provideShellSurfaceRootSimple(final XEventChannel xEventChannel,
														 //strangly enough, we have to use the fully qualified classname here or dagger will produce incorrect code.
														 final org.trinity.foundation.display.x11.impl.XWindowFactory xWindowFactory) {
		return new ShellSurfaceRootSimple(xWindowFactory.create(xEventChannel.getXcbScreen().getRoot()));
	}

	@Provides
	@Singleton
	ShellSimple provideShellSimple() {
		return new ShellSimple();
	}
}
