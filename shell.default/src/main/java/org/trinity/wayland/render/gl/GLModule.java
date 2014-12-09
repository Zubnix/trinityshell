package org.trinity.wayland.render.gl;

import dagger.Module;
import dagger.Provides;
import org.trinity.wayland.WlShmRenderEngine;

import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;

@Module(
        library = true
)
public class GLModule {

    @Provides
    @Singleton
    GLRenderEngineFactory provideGLRenderEngineFactory(){
        return new GLRenderEngineFactory();
    }
}
