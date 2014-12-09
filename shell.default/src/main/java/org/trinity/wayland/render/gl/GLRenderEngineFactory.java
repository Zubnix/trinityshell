package org.trinity.wayland.render.gl;

import com.google.common.util.concurrent.MoreExecutors;
import com.jogamp.common.nio.Buffers;

import javax.inject.Inject;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import java.nio.IntBuffer;
import java.util.concurrent.Executors;

public class GLRenderEngineFactory {

    @Inject
    GLRenderEngineFactory() {
    }

    public GLRenderEngine create(final GLAutoDrawable drawable) {

        makeCurrent(drawable);
        final GL2ES2 gl = drawable.getGL()
                                  .getGL2ES2();
        gl.setSwapInterval(1);
        final IntBuffer elementBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1,
                        elementBuffer);
        final IntBuffer vertexBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1,
                        vertexBuffer);

        return new GLRenderEngine(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor()),
                                  drawable,
                                  elementBuffer,
                                  vertexBuffer);
    }

    private GLContext makeCurrent(final GLAutoDrawable drawable) {
        final GLContext context = drawable.getContext();
        final int current = context.makeCurrent();
        switch (current) {
            case GLContext.CONTEXT_NOT_CURRENT:
                throw new IllegalStateException("GLContext could not be made current.");
            case GLContext.CONTEXT_CURRENT:
            case GLContext.CONTEXT_CURRENT_NEW:
        }
        return context;
    }
}
