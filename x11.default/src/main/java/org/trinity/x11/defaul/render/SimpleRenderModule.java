package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XCompositor;
import org.trinity.x11.defaul.shell.SimpleRootShellSurface;
import org.trinity.x11.defaul.shell.SimpleShellScene;

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
	SimpleShellScene provideSimpleShell(final SimpleRootShellSurface simpleRootShellSurface) {
        return new SimpleShellScene(simpleRootShellSurface);
    }
}
