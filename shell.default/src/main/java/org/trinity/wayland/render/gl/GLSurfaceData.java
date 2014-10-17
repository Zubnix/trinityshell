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

    public void makeActive(final GL2ES2 gl,
                           final ShmBuffer buffer) {

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

    private void paintPixels(ByteBuffer buffer,
                             int padding,
                             int time) {
        final int halfh = padding + (height - padding * 2) / 2;
        final int halfw = padding + (width - padding * 2) / 2;
        int ir;
        int or;
        IntBuffer image = buffer.asIntBuffer();
        image.clear();
        for (int i = 0; i < width * height; ++i) {
            image.put(0xffff0000);
        }
//        image.clear();
//
//        /* squared radii thresholds */
//        or = (halfw < halfh ? halfw : halfh) - 8;
//        ir = or - 32;
//        or = or * or;
//        ir = ir * ir;
//
//        image.position(padding * width);
//        for (int y = padding; y < height - padding; y++) {
//            int y2 = (y - halfh) * (y - halfh);
//
//            image.position(image.position() + padding);
//            for (int x = padding; x < width - padding; x++) {
//                int v;
//
//                int r2 = (x - halfw) * (x - halfw) + y2;
//
//                if (r2 < ir) {
//                    v = (r2 / 32 + time / 64) * 0x0080401;
//                }
//                else if (r2 < or) {
//                    v = (y + time / 32) * 0x0080401;
//                }
//                else {
//                    v = (x + time / 16) * 0x0080401;
//                }
//                v &= 0x00ffffff;
//
//                if (abs(x - y) > 6 && abs(x + y - height) > 6) {
//                    v |= 0xff000000;
//                }
//
//                image.put(v);
//            }
//            image.position(image.position() + padding);
//        }
    }

    private int abs(int i) {
        return i < 0 ? -i : i;
    }

    public IntBuffer getTexture() {
        return tex;
    }
}
