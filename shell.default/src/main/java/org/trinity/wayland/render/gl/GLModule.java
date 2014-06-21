package org.trinity.wayland.render.gl;

import dagger.Module;
import dagger.Provides;
import org.trinity.wayland.WlShmRenderEngine;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 6/10/14.
 */
@Module(
        injects = {
                GLSurfaceDataFactory.class
        },
        library = true
)
public class GLModule {

    private final int width;
    private final int height;

    public GLModule() {
        this.width  = 800;
        this.height = 600;
    }

    @Provides
    @Singleton
    GLRenderEngineFactory provideGLRenderEngineFactory(final Provider<GLSurfaceDataFactory> surfaceDataFactoryProvider){
        return new GLRenderEngineFactory(surfaceDataFactoryProvider);
    }

    @Provides
    @Singleton
    WlShmRenderEngine provideGLRenderEngine(final GLRenderEngineFactory glRenderEngineFactory){
        return glRenderEngineFactory.create(this.width,
                                            this.height);
    }
}
