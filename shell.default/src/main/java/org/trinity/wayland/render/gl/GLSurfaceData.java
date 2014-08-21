package org.trinity.wayland.render.gl;

import com.google.auto.value.AutoValue;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import org.ejml.data.FixedMatrix3x3_64F;
import org.freedesktop.wayland.server.ShmBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLProfile;
import java.nio.ByteBuffer;

import static javax.media.opengl.GL.GL_RGBA;
import static javax.media.opengl.GL.GL_UNSIGNED_BYTE;

@AutoValue
public abstract class GLSurfaceData {

    public static GLSurfaceData create(final Texture texture) {
        return new AutoValue_GLSurfaceData(texture);
    }

    public abstract Texture getTexture();

    public GLSurfaceData refresh(final GLProfile   profile,
                                 final GL2ES2      gl,
                                 final ShmBuffer   buffer) {

        final ByteBuffer bufferData = buffer.getData();
        final int textureWidth      = buffer.getStride() / 4;
        final int textureHeight     = buffer.getHeight();

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
        return this;
    }

    public FixedMatrix3x3_64F calcTransform(){
        final Texture texture = getTexture();
        return new FixedMatrix3x3_64F(1.0/texture.getImageWidth(),
                                      0,
                                      0,

                                      0,
                                      1.0/texture.getImageHeight(),
                                      0,

                                      0,
                                      0,
                                      1);
    }
}
