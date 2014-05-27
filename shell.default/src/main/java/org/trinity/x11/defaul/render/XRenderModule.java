package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
		injects = {
                XCompositor.class,
                XBufferHandlerFactory.class
        },
		complete = false,
		library = true
)
public class XRenderModule {
	@Provides
	@Singleton
    org.trinity.x11.defaul.XCompositor provideXCompositor(final XCompositor XCompositor) {
		return XCompositor;
	}
}
