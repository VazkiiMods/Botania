#version 120

uniform int time; // Passed in, see ShaderHelper.java

uniform float heightMatch; // Passed in via Callback
uniform sampler2D image;
uniform sampler2D mask;

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(image, texcoord);
    vec4 maskColor = texture2D(mask, texcoord);
    float maskgs = (maskColor.r + maskColor.g + maskColor.b) / 3.0;

    if(maskgs <= heightMatch)
    	gl_FragColor = vec4(color.r, color.g, color.b, color.a);
    else {
        float gs = (color.r + color.g + color.b) / 3.0;
        gl_FragColor = vec4(gs, gs, gs, color.a * 0.75);
    }
}