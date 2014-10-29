package org.trinity.wayland.render.gl;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.opengl.*;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Singleton
public class GLRenderEngineFactory {

    @Inject
    GLRenderEngineFactory() {
    }

    public Future<GLRenderEngine> create(final int width,
                                         final int height) throws ExecutionException, InterruptedException {

        //use a separate thread to do rendering to keep gl contexts unpolluted
        final ListeningExecutorService renderThread = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        return renderThread.submit(() -> doCreate(renderThread,
                                                  width,
                                                  height));
    }

    private GLRenderEngine doCreate(final ListeningExecutorService renderThread,
                                    final int width,
                                    final int height) throws ExecutionException, InterruptedException {
        final GLProfile profile = getGLProfile();
        final GLAutoDrawable drawable = createDrawable(System.getenv("DISPLAY"),
                                                       profile,
                                                       width,
                                                       height);
        makeCurrent(drawable);
        final GL2ES2 gl = drawable.getGL()
                                  .getGL2ES2();
        gl.setSwapInterval(1);
        IntBuffer elementBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1,
                        elementBuffer);
        IntBuffer vertexBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1,
                        vertexBuffer);

        return new GLRenderEngine(renderThread,
                                  drawable,
                                  elementBuffer,
                                  vertexBuffer);
    }

    private GLProfile getGLProfile() {
        return GLProfile.getGL2ES2();
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

    private GLAutoDrawable createDrawable(final String xDisplay,
                                          final GLProfile profile,
                                          final int width,
                                          final int height) {
        final Display display = NewtFactory.createDisplay(xDisplay);
        final Screen screen = NewtFactory.createScreen(display,
                                                       0);
        final GLWindow drawable = GLWindow.create(screen,
                                                  new GLCapabilities(profile));
        drawable.setSize(width,
                         height);
        drawable.setVisible(true,
                            true);
        return drawable;
    }
}
