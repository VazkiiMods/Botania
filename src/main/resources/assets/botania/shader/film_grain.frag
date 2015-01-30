#version 120
#extension GL_EXT_gpu_shader4 : enable

uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

float rand(vec2 co) {
   return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    float gs = rand(vec2(int(texcoord.x * 512) ^ time, int(texcoord.y * 512) ^ ~time)) * 0.5;
    
    gl_FragColor = vec4(gs, gs, gs, color.a);
}