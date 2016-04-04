#version 120

uniform float alpha; // Passed in by callback
uniform sampler2D tex;
uniform int time; // Passed in, see ShaderHelper.java

void main() {
    gl_FragColor = texture2D(tex, vec2(gl_TexCoord[0])) * gl_Color * vec4(1.0, 1.0, 1.0, alpha);
}