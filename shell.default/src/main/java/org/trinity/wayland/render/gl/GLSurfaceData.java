package org.trinity.wayland.render.gl;

import com.jogamp.common.nio.Buffers;
import org.freedesktop.wayland.server.ShmBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GLSurfaceData {

    public static GLSurfaceData create(final GL2ES2 gl) {
        IntBuffer tex = Buffers.newDirectIntBuffer(1);
        gl.glGenTextures(1,
                         tex);
        return new GLSurfaceData(gl,
                                 tex);
    }

    private final IntBuffer tex;
    private int width;
    private int height;

    private GLSurfaceData(final GL2ES2 gl,
                          final IntBuffer tex) {
        this.tex = tex;
    }

    public void init(final GL2ES2 gl,
                     final ShmBuffer buffer){
        this.width = buffer.getStride() / 4;
        this.height = buffer.getHeight();
        final ByteBuffer pixels = buffer.getData();

        gl.glBindTexture(GL2ES2.GL_TEXTURE_2D,
                         getTexture().get(0));
        gl.glTexImage2D(GL.GL_TEXTURE_2D,
                        0,
                        GL.GL_RGBA,
                        width,
                        height,
                        0,
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        pixels);

        gl.glTexParameteri(GL.GL_TEXTURE_2D,
                           GL.GL_TEXTURE_WRAP_S,
                           GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,
                           GL.GL_TEXTURE_WRAP_T,
                           GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,
                           GL.GL_TEXTURE_MIN_FILTER,
                           GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D,
                           GL.GL_TEXTURE_MAG_FILTER,
                           GL.GL_NEAREST);
    }

    public void makeActive(final GL2ES2 gl,
                           final ShmBuffer buffer) {

        this.width = buffer.getStride() / 4;
        this.height = buffer.getHeight();
        final ByteBuffer pixels = buffer.getData();

        gl.glBindTexture(GL2ES2.GL_TEXTURE_2D,
                         getTexture().get(0));
        gl.glTexSubImage2D(GL.GL_TEXTURE_2D,
                           0,
                           0,
                           0,
                           width,
                           height,
                           GL.GL_RGBA,
                           GL.GL_UNSIGNED_BYTE,
                           pixels);
//
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,
//                           GL.GL_TEXTURE_WRAP_S,
//                           GL.GL_CLAMP_TO_EDGE);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,
//                           GL.GL_TEXTURE_WRAP_T,
//                           GL.GL_CLAMP_TO_EDGE);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,
//                           GL.GL_TEXTURE_MIN_FILTER,
//                           GL.GL_NEAREST);
//        gl.glTexParameteri(GL.GL_TEXTURE_2D,
//                           GL.GL_TEXTURE_MAG_FILTER,
//                           GL.GL_NEAREST);
    }

    public IntBuffer getTexture() {
        return tex;
    }
}
