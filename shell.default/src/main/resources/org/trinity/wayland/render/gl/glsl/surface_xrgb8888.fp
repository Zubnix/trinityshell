//FRAGMENT SHADER
#version 100
uniform sampler2D fu_texture;

varying mediump vec2 vo_tex_coord;

void main() {
    gl_FragColor = vec4(texture2D(fu_texture, vo_tex_coord.xy).bgr, 1);
}