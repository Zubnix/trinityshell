package org.trinity.wayland.defaul.render.gl;

import dagger.Module;
import dagger.Provides;
import org.trinity.wayland.defaul.WlShmRenderEngine;

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

    public GLModule(final int width,
                    final int height) {
        this.width  = width;
        this.height = height;
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
