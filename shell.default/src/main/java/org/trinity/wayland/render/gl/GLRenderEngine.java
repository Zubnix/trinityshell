package org.trinity.wayland.render.gl;

import com.google.common.collect.Maps;
import com.hackoeur.jglm.Mat4;
import com.jogamp.common.nio.Buffers;
import org.freedesktop.wayland.server.ShmBuffer;
import org.freedesktop.wayland.shared.WlShmFormat;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.WlShmRenderEngine;

import javax.inject.Inject;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import java.nio.IntBuffer;
import java.util.Map;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static org.trinity.wayland.render.gl.GLBufferFormat.SHM_ARGB8888;
import static org.trinity.wayland.render.gl.GLBufferFormat.SHM_XRGB8888;

public class GLRenderEngine implements WlShmRenderEngine {

    private static final String SURFACE_V          =
            "uniform mat4 mu_projection;\n" +
                    "\n" +
                    "attribute vec2 va_position;\n" +
                    "attribute vec2 va_texcoord;\n" +
                    "\n" +
                    "varying vec2 vv_texcoord;\n" +
                    "\n" +
                    "void main(){\n" +
                    "    vv_texcoord = va_texcoord;\n" +
                    "    gl_Position = vec4(va_position, 0.0, 1.0) * mu_projection;\n" +
                    "}";
    private static final String SURFACE_ARGB8888_F =
            "varying vec2 vv_texcoord;\n" +
                    "uniform sampler2D tex;\n" +
                    "\n" +
                    "void main(){\n" +
                    "    gl_FragColor = texture2D(tex, vv_texcoord).bgra;\n" +
                    "}";
    private static final String SURFACE_XRGB8888_F =
            "varying vec2 vv_texcoord;\n" +
                    "uniform sampler2D tex;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(texture2D(tex, vv_texcoord).bgr, 1);\n" +
                    "}";

    private final Map<ShellSurface, GLSurfaceData> cachedSurfaceData = Maps.newHashMap();
    private final Map<GLBufferFormat, Integer>     shaderPrograms    = Maps.newHashMap();

    private final GLAutoDrawable drawable;
    private       Mat4           projection;


    @Inject
    GLRenderEngine(final GLAutoDrawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void begin() {
        projection = new Mat4(2.0f / this.drawable.getSurfaceWidth(),
                              0,
                              0,
                              0,

                              0,
                              2.0f / -this.drawable.getSurfaceHeight(),
                              0,
                              0,

                              0,
                              0,
                              -1,
                              0,

                              -1,
                              1,
                              0,
                              1);
        final GL2ES2 gl = queryGl();
        makeCurrent();
        clear(gl);
    }

    @Override
    public void draw(final ShellSurface shellSurface,
                     final ShmBuffer buffer) {
        final GL2ES2 gl = queryGl();

        buffer.beginAccess();
        final GLSurfaceData surfaceData = querySurfaceData(gl,
                                                           shellSurface).makeActive(gl,
                                                                                    buffer);
        final PointImmutable position = shellSurface.getPosition();
        float[] vertices = {
                position.getX(),
                position.getY(),
                0f,
                0f,

                position.getX() + surfaceData.getWidth(),
                position.getY(),
                1f,
                0f,

                position.getX(),
                position.getY() - surfaceData.getHeight(),
                0f,
                1f,

                position.getX() + surfaceData.getWidth(),
                position.getY() - surfaceData.getHeight(),
                1f,
                1f,
        };

        final int shaderProgram = queryShaderProgram(gl,
                                                     queryBufferFormat(buffer));
        configureShaders(gl,
                         shaderProgram,
                         projection,
                         vertices);
        gl.glUseProgram(shaderProgram);
        //TODO use element buffer?
        gl.glDrawArrays(GL_TRIANGLES,
                        0,
                        6);
        buffer.endAccess();
    }

    @Override
    public void end() {
        this.drawable.swapBuffers();
    }

    private int queryShaderProgram(final GL2ES2 gl,
                                   GLBufferFormat bufferFormat) {
        Integer shaderProgram = this.shaderPrograms.get(bufferFormat);
        if (shaderProgram == null) {
            shaderProgram = createShaderProgram(gl,
                                                bufferFormat);
            this.shaderPrograms.put(bufferFormat,
                                    shaderProgram);
        }
        return shaderProgram;
    }

    private int createShaderProgram(final GL2ES2 gl,
                                    GLBufferFormat bufferFormat) {
        final int vertexShader = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
        compileShader(gl,
                      vertexShader,
                      SURFACE_V);

        final int fragmentShader;
        if (bufferFormat == SHM_ARGB8888) {
            fragmentShader = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
            compileShader(gl,
                          fragmentShader,
                          SURFACE_ARGB8888_F);
        }
        else if (bufferFormat == SHM_XRGB8888) {
            fragmentShader = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
            compileShader(gl,
                          fragmentShader,
                          SURFACE_XRGB8888_F);
        }
        else {
            throw new UnsupportedOperationException("Buffer format " + bufferFormat + " is not supported");
        }

        final int shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram,
                          vertexShader);
        gl.glAttachShader(shaderProgram,
                          fragmentShader);
        gl.glLinkProgram(shaderProgram);
        return shaderProgram;
    }

    private void compileShader(final GL2ES2 gl,
                               final int shaderHandle,
                               String shaderSource) {
        String[] lines = new String[]{shaderSource};
        int[] lengths = new int[]{lines[0].length()};
        gl.glShaderSource(shaderHandle,
                          lines.length,
                          lines,
                          lengths,
                          0);
        gl.glCompileShader(shaderHandle);

        IntBuffer vstatus = IntBuffer.allocate(1);
        gl.glGetShaderiv(shaderHandle,
                         GL2ES2.GL_COMPILE_STATUS,
                         vstatus);
        if (vstatus.get(0) == GL.GL_TRUE) {
            //success
        }
        else {
            //failure!
            //get log length
            int[] logLength = new int[1];
            gl.glGetShaderiv(shaderHandle,
                             GL2ES2.GL_INFO_LOG_LENGTH,
                             logLength,
                             0);
            //get log
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(shaderHandle,
                                  logLength[0],
                                  (int[]) null,
                                  0,
                                  log,
                                  0);
            System.err.println("Error compiling the vertex shader: " + new String(log));
            System.exit(1);
        }
    }

    private void configureShaders(final GL2ES2 gl,
                                  final Integer program,
                                  final Mat4 projection,
                                  final float[] vertices) {

        int uniTrans = gl.glGetUniformLocation(program,
                                               "mu_projection");
        gl.glUniformMatrix4fv(uniTrans,
                              1,
                              false,
                              projection.getBuffer());

        //make vertices_buffer active
        IntBuffer buffer = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1,
                        buffer);
        //make buffer active
        gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER,
                        buffer.get(0));
        gl.glBufferData(GL2ES2.GL_ARRAY_BUFFER,
                        vertices.length * 4,
                        Buffers.newDirectFloatBuffer(vertices),
                        GL2ES2.GL_DYNAMIC_DRAW);
        int posAttrib = gl.glGetAttribLocation(program,
                                               "va_position");
        int texAttrib = gl.glGetAttribLocation(program,
                                               "va_texcoord");
        gl.glEnableVertexAttribArray(posAttrib);
        gl.glVertexAttribPointer(posAttrib,
                                 2,
                                 GL2ES2.GL_FLOAT,
                                 false,
                                 4 * 4,
                                 0);
        gl.glEnableVertexAttribArray(texAttrib);
        gl.glVertexAttribPointer(texAttrib,
                                 2,
                                 GL.GL_FLOAT,
                                 false,
                                 4 * 4,
                                 2 * 4);
    }

    private GL2ES2 queryGl() {
        return this.drawable.getGL()
                            .getGL2ES2();
    }

    private void clear(final GL2ES2 gl) {
        //set everything to blue when doing a render pass, eases debugging.
        gl.glClearColor(0,
                        0,
                        1,
                        1);
        gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void makeCurrent() {
        final int current = this.drawable.getContext()
                                         .makeCurrent();
        switch (current) {
            case GLContext.CONTEXT_NOT_CURRENT:
                throw new IllegalStateException("GLContext could not be made current.");
            case GLContext.CONTEXT_CURRENT:
            case GLContext.CONTEXT_CURRENT_NEW:
        }
    }

    private GLBufferFormat queryBufferFormat(final ShmBuffer buffer) {
        final GLBufferFormat format;
        final int bufferFormat = buffer.getFormat();
        if (bufferFormat == WlShmFormat.ARGB8888.getValue()) {
            format = SHM_ARGB8888;
        }
        else if (bufferFormat == WlShmFormat.XRGB8888.getValue()) {
            format = SHM_XRGB8888;
        }
        else {
            throw new UnsupportedOperationException("Format " + buffer.getFormat() + " not supported.");
        }
        return format;
    }

    private GLSurfaceData querySurfaceData(final GL2ES2 gl,
                                           final ShellSurface shellSurface) {
        GLSurfaceData surfaceData = this.cachedSurfaceData.get(shellSurface);
        if (surfaceData == null) {
            surfaceData = GLSurfaceData.create(gl);
        }
        return surfaceData;
    }
}
