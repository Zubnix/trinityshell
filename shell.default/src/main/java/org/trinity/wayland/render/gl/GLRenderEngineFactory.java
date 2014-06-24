package org.trinity.wayland.render.gl;

import com.google.common.collect.Maps;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import java.util.Map;

@Singleton
public class GLRenderEngineFactory {

    private final Provider<GLSurfaceDataFactory> surfaceDataFactoryProvider;

    @Inject
    GLRenderEngineFactory(final Provider<GLSurfaceDataFactory> surfaceDataFactoryProvider) {
        this.surfaceDataFactoryProvider = surfaceDataFactoryProvider;
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
        return new GLRenderEngine(this.surfaceDataFactoryProvider.get(),
                                  createShaders(drawable),
                                  drawable,
                                  profile);
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
        //set everything to green when initialising, eases debugging.
        gl.glClearColor(0,
                        1,
                        0,
                        1);
        gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_ONE,
                GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glViewport(0,
                      0,
                      width,
                      height);
        drawable.swapBuffers();

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

    private Map<GLBufferFormat, ShaderState> createShaders(final GLAutoDrawable drawable) {
        final GLContext context = makeCurrent(drawable);
        final GL2ES2 gl         = drawable.getGL()
                                          .getGL2ES2();
        final Map<GLBufferFormat, ShaderState> shaders = Maps.newHashMap();
        for(final GLBufferFormat type : GLBufferFormat.values()) {
            final ShaderCode vertexShader   = ShaderCode.create(gl,
                                                                GL2ES2.GL_VERTEX_SHADER,
                                                                this.getClass(),
                                                                "glsl",
                                                                null,
                                                                type.getVertexShader(),
                                                                false);
            final ShaderCode fragmentShader = ShaderCode.create(gl,
                                                                GL2ES2.GL_FRAGMENT_SHADER,
                                                                this.getClass(),
                                                                "glsl",
                                                                null,
                                                                type.getFragmentShader(),
                                                                false);
            final ShaderProgram shaderProgram = new ShaderProgram();
            shaderProgram.add(vertexShader);
            shaderProgram.add(fragmentShader);
            if(!shaderProgram.link(gl,
                          System.err)) {
                throw new GLException("Couldn't link program: " + shaderProgram);
            }
            final ShaderState state = new ShaderState();
            state.attachShaderProgram(gl,
                                      shaderProgram,
                                      false);
            shaders.put(type,
                        state);
        }
        context.release();
        return shaders;
    }
}
