package org.trinity.wayland.defaul.render.gl;

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
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import java.util.Map;

import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;

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
        final GLAutoDrawable drawable = createDrawable(profile,
                                                       width,
                                                       height);

        return new GLRenderEngine(this.surfaceDataFactoryProvider.get(),
                              createShaders(drawable.getGL()
                                                    .getGL2ES2()),
                              drawable,
                              profile
        );
    }

    private GLProfile getGLProfile() {
        return GLProfile.getGL2ES2();
    }

    private GLAutoDrawable createDrawable(final GLProfile profile,
                                          final int       width,
                                          final int       height) {
        final Display display = NewtFactory.createDisplay(":0.0");
        final Screen screen = NewtFactory.createScreen(display,
                                                       0);
        final GLWindow drawable = GLWindow.create(screen,
                                                  new GLCapabilities(profile));

        final GL2ES2 gl = drawable.getGL()
                                  .getGL2ES2();
        gl.setSwapInterval(1);
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
        return drawable;
    }

    private Map<GLBufferFormat, ShaderState> createShaders(final GL2ES2 gl) {
        final Map<GLBufferFormat, ShaderState> shaders = Maps.newHashMap();
        for(final GLBufferFormat type : GLBufferFormat.values()) {
            final ShaderCode vcode = ShaderCode.create(gl,
                                                       GL_VERTEX_SHADER,
                                                       this.getClass(),
                                                       "glsl",
                                                       null,
                                                       type.getVertexShader(),
                                                       false);
            final ShaderCode fcode = ShaderCode.create(gl,
                                                       GL_FRAGMENT_SHADER,
                                                       this.getClass(),
                                                       "glsl",
                                                       null,
                                                       type.getFragmentShader(),
                                                       false);

            final ShaderProgram prog = new ShaderProgram();
            prog.add(vcode);
            prog.add(fcode);
            if(!prog.link(gl,
                          System.err)) { throw new GLException("Couldn't link program: " + prog); }

            final ShaderState state = new ShaderState();
            state.attachShaderProgram(gl,
                                      prog,
                                      false);

            shaders.put(type,
                        state);
        }
        return shaders;
    }
}
