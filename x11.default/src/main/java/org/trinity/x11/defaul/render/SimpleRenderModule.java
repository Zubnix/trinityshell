package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XCompositor;

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
    SimpleRenderer provideSimpleShell() {
        return new SimpleRenderer();
    }
}
