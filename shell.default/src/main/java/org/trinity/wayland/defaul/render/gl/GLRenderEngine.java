package org.trinity.wayland.defaul.render.gl;

import com.google.common.collect.Maps;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.Texture;
import org.ejml.data.FixedMatrix3x3_64F;
import org.ejml.data.FixedMatrix4x4_64F;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.defaul.WlShmRenderEngine;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

import javax.inject.Inject;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLUniformData;
import java.nio.FloatBuffer;
import java.util.Map;

import static javax.media.opengl.GL.*;
import static org.freedesktop.wayland.protocol.wl_shm.FORMAT_ARGB8888;
import static org.freedesktop.wayland.protocol.wl_shm.FORMAT_XRGB8888;
import static org.trinity.wayland.defaul.render.gl.GLBufferFormat.SHM_ARGB8888;
import static org.trinity.wayland.defaul.render.gl.GLBufferFormat.SHM_XRGB8888;

public class GLRenderEngine implements WlShmRenderEngine {

    private final Map<ShellSurface, GLSurfaceData> cachedSurfaceData = Maps.newHashMap();

    private final GLSurfaceDataFactory             glSurfaceDataFactory;
    private final GLAutoDrawable                   drawable;
    private final GLProfile                        profile;
    private final Map<GLBufferFormat, ShaderState> shaders;

    @Inject
    GLRenderEngine(final GLSurfaceDataFactory             glSurfaceDataFactory,
                   final Map<GLBufferFormat, ShaderState> shaders,
                   final GLAutoDrawable                   drawable,
                   final GLProfile                        profile) {
        this.shaders              = shaders;
        this.glSurfaceDataFactory = glSurfaceDataFactory;
        this.drawable             = drawable;
        this.profile              = profile;
    }

    @Override
    public void draw(final ShellSurface shellSurface,
                     final WlShmBuffer  buffer) {

        makeCurrent();
        final GL2ES2 gl = queryGl();

        final GLSurfaceData surfaceData = querySurfaceData(gl,
                                                           shellSurface).refresh(this.profile,
                                                                                 gl,
                                                                                 buffer);

        clear(gl);
        final ShaderState state = this.shaders.get(queryBufferFormat(buffer));
        enableShader(gl,
                     state,
                     new FixedMatrix4x4_64F(2.0 / this.drawable.getWidth(),
                                            0,
                                            0,
                                            0,

                                            0,
                                            2.0 / -this.drawable.getHeight(),
                                            0,
                                            0,

                                            0,
                                            0,
                                            -1,
                                            0,

                                            -1,
                                            1,
                                            0,
                                            1),
                     surfaceData.calcTransform(),
                     shellSurface.getTransform()
                    );
        draw(gl,
             surfaceData.getTexture(),
             buffer.getSize());
        disableShader(gl,
                      state);
    }

    private void draw(final GL2ES2             gl,
                      final Texture            texture,
                      final DimensionImmutable size) {
        gl.glActiveTexture(GL_TEXTURE0);
        texture.bind(gl);

        final int width = size.getWidth();
        final int height = size.getHeight();

        final float topLeftX = 0;
        final float topLeftY = 0;

        final float bottomLeftX = 0;
        final float bottomLeftY = height;

        final float topRightX = width;
        final float topRightY = 0;

        final float bottomRightX = width;
        final float bottomRightY = height;
        final float[] vertices = {
                //first triangle
                topLeftX,
                topLeftY,
                bottomLeftX,
                bottomLeftY,
                topRightX,
                topRightY,
                //second triangle
                bottomLeftX,
                bottomLeftY,
                topRightX,
                topRightY,
                bottomRightX,
                bottomRightY
        };

        final GLArrayDataClient vertexData = GLArrayDataClient.createGLSL("va_vertex",
                                                                          2,
                                                                          GL_FLOAT,
                                                                          false,
                                                                          6);
        vertexData.setName("va_vertex");
        ((FloatBuffer) vertexData.getBuffer()).put(vertices);
        vertexData.seal(true);
        vertexData.enableBuffer(gl,
                                true);

        gl.glDrawArrays(GL_TRIANGLES,
                        0,
                        6);
    }

    private void disableShader(final GL2ES2      gl,
                               final ShaderState shaderState) {
        shaderState.useProgram(gl,
                               false);
    }

    private void enableShader(final GL2ES2             gl,
                              final ShaderState        state,
                              final FixedMatrix4x4_64F projection,
                              final FixedMatrix3x3_64F textureTransform,
                              final FixedMatrix3x3_64F bufferTransform) {
        state.useProgram(gl,
                         true);
        state.uniform(gl,
                new GLUniformData("vu_projection",
                        4,
                        4,
                        FloatBuffer.wrap(new float[]{
                                (float) projection.a11,
                                (float) projection.a12,
                                (float) projection.a13,
                                (float) projection.a14,

                                (float) projection.a21,
                                (float) projection.a22,
                                (float) projection.a23,
                                (float) projection.a24,

                                (float) projection.a31,
                                (float) projection.a32,
                                (float) projection.a33,
                                (float) projection.a34,

                                (float) projection.a41,
                                (float) projection.a42,
                                (float) projection.a43,
                                (float) projection.a44
                        })
                )
        );

        state.uniform(gl,
                      new GLUniformData("vu_surface_transform",
                                        3,
                                        3,
                                        FloatBuffer.wrap(new float[]{
                                                (float) bufferTransform.a11,
                                                (float) bufferTransform.a12,
                                                (float) bufferTransform.a13,

                                                (float) bufferTransform.a21,
                                                (float) bufferTransform.a22,
                                                (float) bufferTransform.a23,

                                                (float) bufferTransform.a31,
                                                (float) bufferTransform.a32,
                                                (float) bufferTransform.a33
                                        })
                      )
                     );
        state.uniform(gl,
                      new GLUniformData("vu_texture_transform",
                                        3,
                                        3,
                                        FloatBuffer.wrap(new float[]{
                                                (float) textureTransform.a11,
                                                (float) textureTransform.a12,
                                                (float) textureTransform.a13,

                                                (float) textureTransform.a21,
                                                (float) textureTransform.a22,
                                                (float) textureTransform.a23,

                                                (float) textureTransform.a31,
                                                (float) textureTransform.a32,
                                                (float) textureTransform.a33
                                        })
                      )
                     );

        state.uniform(gl,
                      new GLUniformData("fu_texture",
                      0));
    }

    private GL2ES2 queryGl() {
        return this.drawable.getContext()
                            .getGL()
                            .getGL2ES2();
    }

    private void clear(final GL2ES2 gl) {
        gl.glClearColor(0,
                        0,
                        1,
                        1);
        gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void makeCurrent() {
        final int status = this.drawable.getContext()
                                        .makeCurrent();
        if(status != GLContext.CONTEXT_CURRENT) {
            throw new IllegalStateException();
        }
    }

    private GLBufferFormat queryBufferFormat(final WlShmBuffer buffer) {
        final GLBufferFormat format;
        switch(buffer.getFormat()) {
            case FORMAT_ARGB8888: {
                format = SHM_ARGB8888;
                break;
            }
            case FORMAT_XRGB8888: {
                format = SHM_XRGB8888;
                break;
            }
            default: {
                throw new UnsupportedOperationException("Format " + buffer.getFormat() + " not supported.");
            }
        }
        return format;
    }

    private GLSurfaceData querySurfaceData(final GL2ES2       gl,
                                           final ShellSurface shellSurface) {
        GLSurfaceData surfaceData = this.cachedSurfaceData.get(shellSurface);
        if(surfaceData == null) {
            surfaceData = this.glSurfaceDataFactory.create(gl);
        }
        return surfaceData;
    }
}
