#version 120

uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    
    float c = sin(time / 80.0 + sin(texcoord.x / 5 + (texcoord.y - cos(time / 300.0) * 0.25) / ((1 - sin(time / 200.0) / 10.0) * 5)) * sin(time / 600.0) * 1000) * 0.5 + 0.5;
    float g = 1 - c * 0.2 - 0.2;
    float b = 1 - c * 0.2;
    gl_FragColor = vec4(0, g, b, color.a);
}