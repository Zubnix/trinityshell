package org.trinity.foundation.display.x11.impl.render.simple;

import dagger.Module;
import dagger.Provides;
import org.trinity.foundation.display.x11.impl.XCompositor;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XWindowFactory;

import javax.inject.Singleton;

@Module(
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
														 final XWindowFactory xWindowFactory) {
		return new ShellSurfaceRootSimple(xWindowFactory.create(xEventChannel.getXcbScreen().getRoot()));
	}

	@Provides
	@Singleton
	ShellSimple provideShellSimple() {
		return new ShellSimple();
	}
}
