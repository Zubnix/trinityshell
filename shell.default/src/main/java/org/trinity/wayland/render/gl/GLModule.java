package org.trinity.wayland.render.gl;

import dagger.Module;
import dagger.Provides;
import org.trinity.wayland.WlShmRenderEngine;

import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;

/**
 * Created by Erik De Rijcke on 6/10/14.
 */
@Module(
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
    GLRenderEngineFactory provideGLRenderEngineFactory(){
        return new GLRenderEngineFactory();
    }

    @Provides
    @Singleton
    WlShmRenderEngine provideGLRenderEngine(final GLRenderEngineFactory glRenderEngineFactory){
        try {
            return glRenderEngineFactory.create(this.width,
                                                this.height).get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
