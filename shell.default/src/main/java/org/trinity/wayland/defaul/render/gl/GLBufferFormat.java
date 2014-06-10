package org.trinity.wayland.defaul.render.gl;

public enum GLBufferFormat {
    SHM_ARGB8888("surface",
                 "surface_argb8888"),
    SHM_XRGB8888("surface",
                 "surface_xrgb8888");

    private final String vertexShader;
    private final String fragmentShader;

    GLBufferFormat(final String vertexShader,
                   final String fragmentShader) {
        this.vertexShader   = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public String getFragmentShader() {
        return this.fragmentShader;
    }

    public String getVertexShader() {
        return this.vertexShader;
    }
}
