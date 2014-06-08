package org.trinity.wayland.defaul.render.gl;

import javax.inject.Inject;

//This is a shameless modified copy of the glrenderer by jason ekstrand from wilee-jogl on github.
//Copied because I have no idea how to properly do this :)
public class GLRenderer {
    private final GLSurfaceDataFactory glSurfaceDataFactory;

    @Inject
    GLRenderer(final GLSurfaceDataFactory glSurfaceDataFactory) {
        this.glSurfaceDataFactory = glSurfaceDataFactory;
    }
}
