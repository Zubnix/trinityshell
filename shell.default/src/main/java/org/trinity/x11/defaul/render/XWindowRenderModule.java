package org.trinity.x11.defaul.render;

import dagger.Module;
import dagger.Provides;
import org.trinity.x11.defaul.XCompositor;

import javax.inject.Singleton;

@Module(
		injects = {
                XWindowCompositor.class,
                XWindowBufferHandlerFactory.class
        },
		complete = false,
		library = true
)
public class XWindowRenderModule {
	@Provides
	@Singleton
	XCompositor provideXCompositor(final XWindowCompositor XWindowCompositor) {
		return XWindowCompositor;
	}
}
