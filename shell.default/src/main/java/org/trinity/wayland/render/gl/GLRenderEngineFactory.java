package org.trinity.wayland.render.gl;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.opengl.*;

@Singleton
public class GLRenderEngineFactory {

    @Inject
    GLRenderEngineFactory() {
    }

    public GLRenderEngine create(final int width,
                                 final int height) {
        final GLProfile profile = getGLProfile();
        final GLAutoDrawable drawable = configureDrawable(createDrawable(System.getenv("DISPLAY"),
                                                                         profile,
                                                                         width,
                                                                         height),
                                                          width,
                                                          height);
        return new GLRenderEngine(drawable);
    }

    private GLProfile getGLProfile() {
        return GLProfile.getGL2ES2();
    }

    private GLAutoDrawable configureDrawable(final GLAutoDrawable drawable,
                                             final int            width,
                                             final int            height){
        final GLContext context = makeCurrent(drawable);
        final GL2ES2 gl         = drawable.getGL()
                                          .getGL2ES2();
        gl.setSwapInterval(1);
        context.release();

        return drawable;
    }

    private GLContext makeCurrent(final GLAutoDrawable drawable){
        final GLContext context = drawable.getContext();
        final int current = context.makeCurrent();
        switch (current){
            case GLContext.CONTEXT_NOT_CURRENT:
                throw new IllegalStateException("GLContext could not be made current.");
            case GLContext.CONTEXT_CURRENT:
            case GLContext.CONTEXT_CURRENT_NEW:
        }
        return context;
    }

    private GLAutoDrawable createDrawable(final String    xDisplay,
                                          final GLProfile profile,
                                          final int       width,
                                          final int       height) {
        final Display display = NewtFactory.createDisplay(xDisplay);
        final Screen screen   = NewtFactory.createScreen(display,
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
