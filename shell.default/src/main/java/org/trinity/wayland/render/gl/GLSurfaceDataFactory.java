package org.trinity.wayland.render.gl;

import com.jogamp.opengl.util.texture.Texture;

import javax.inject.Inject;
import javax.media.opengl.GL2ES2;

public class GLSurfaceDataFactory {

    @Inject
    GLSurfaceDataFactory() {
    }

    public GLSurfaceData create(final GL2ES2 gl){
        return GLSurfaceData.create(createTexture(gl));
    }

    private Texture createTexture(final GL2ES2 gl) {
        final Texture texture = new Texture(GL2ES2.GL_TEXTURE_2D);
        texture.setTexParameterf(gl,
                                 GL2ES2.GL_TEXTURE_WRAP_S,
                                 GL2ES2.GL_CLAMP_TO_EDGE);
        texture.setTexParameterf(gl,
                                 GL2ES2.GL_TEXTURE_WRAP_T,
                                 GL2ES2.GL_CLAMP_TO_EDGE);
        texture.setTexParameterf(gl,
                                 GL2ES2.GL_TEXTURE_MAG_FILTER,
                                 GL2ES2.GL_NEAREST);
        texture.setTexParameterf(gl,
                                 GL2ES2.GL_TEXTURE_MIN_FILTER,
                                 GL2ES2.GL_NEAREST);

        return texture;
    }
}
