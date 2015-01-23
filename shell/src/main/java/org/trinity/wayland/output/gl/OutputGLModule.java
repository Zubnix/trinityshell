package org.trinity.wayland.output.gl;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        library = true
)
public class OutputGLModule {

    @Provides
    @Singleton
    GLRenderEngineFactory provideGLRenderEngineFactory() {
        return new GLRenderEngineFactory();
    }
}
