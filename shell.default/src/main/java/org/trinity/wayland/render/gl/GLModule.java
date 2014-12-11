package org.trinity.wayland.render.gl;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        library = true
)
public class GLModule {

    @Provides
    @Singleton
    GLRenderEngineFactory provideGLRenderEngineFactory() {
        return new GLRenderEngineFactory();
    }
}
