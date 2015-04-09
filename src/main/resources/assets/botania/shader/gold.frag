#version 120

uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    float gs = (color.r + color.g + color.g) / 3.0;
    
    float rm = 1.0;
    float gm = 0.85;
    float bm = 0.07;
    
    gl_FragColor = vec4(gs * rm, gs * gm, gs * bm, color.a);
}