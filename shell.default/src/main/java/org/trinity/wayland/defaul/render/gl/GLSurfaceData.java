package org.trinity.wayland.defaul.render.gl;

import com.google.auto.value.AutoValue;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLProfile;
import java.nio.ByteBuffer;

import static javax.media.opengl.GL.GL_RGBA;
import static javax.media.opengl.GL.GL_UNSIGNED_BYTE;
import static org.freedesktop.wayland.protocol.wl_shm.FORMAT_ARGB8888;
import static org.freedesktop.wayland.protocol.wl_shm.FORMAT_XRGB8888;
import static org.trinity.wayland.defaul.render.gl.GLBufferFormat.SHM_ARGB8888;
import static org.trinity.wayland.defaul.render.gl.GLBufferFormat.SHM_XRGB8888;

@AutoValue
public abstract class GLSurfaceData {

    public static GLSurfaceData create(final Texture texture) {
        return new AutoValue_GLSurfaceData(texture);
    }

    public abstract Texture getTexture();

    public GLBufferFormat refresh(final GLProfile   profile,
                                  final GL2ES2      gl,
                                  final WlShmBuffer buffer) {
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

        final ByteBuffer bufferData = buffer.getByteBuffer();
        final int textureWidth      = buffer.getStride() / 4;
        final int textureHeight     = buffer.getSize()
                                            .getHeight();

        final TextureData textureData = new TextureData(profile,
                                                        GL_RGBA,
                                                        textureWidth,
                                                        textureHeight,
                                                        0,
                                                        GL_RGBA,
                                                        GL_UNSIGNED_BYTE,
                                                        false,
                                                        false,
                                                        false,
                                                        bufferData,
                                                        null);
        getTexture().bind(gl);
        getTexture().updateImage(gl,
                                 textureData);

        return format;
    }
}
