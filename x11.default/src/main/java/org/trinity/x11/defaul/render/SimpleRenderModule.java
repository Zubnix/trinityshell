package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XCompositor;
import org.trinity.x11.defaul.XEventChannel;

import javax.inject.Singleton;

@Module(
		injects = {
			SimpleXCompositor.class
		},
		complete = false,
		library = true
)
public class SimpleRenderModule {
	@Provides
	@Singleton
	XCompositor provideXCompositor(final SimpleXCompositor simpleXCompositor) {
		return simpleXCompositor;
	}

	@Provides
	@Singleton
    SimpleShellSurfaceRoot provideShellSurfaceRootSimple(final XEventChannel xEventChannel,
														 //strangly enough, we have to use the fully qualified classname here or dagger will produce incorrect code.
														 final org.trinity.foundation.display.x11.impl.XWindowFactory xWindowFactory) {
		return new SimpleShellSurfaceRoot(xWindowFactory.create(xEventChannel.getXcbScreen().getRoot()));
	}

	@Provides
	@Singleton
    SimpleShell provideShellSimple() {
		return new SimpleShell();
	}
}
