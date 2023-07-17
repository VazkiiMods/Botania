#version 150

#moj_import <fog.glsl>
// [VanillaCopy] particle.fsh with changes

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    float gs = rand(vec2(int(texCoord0.x * 256), int(texCoord0.y * 512))) * 0.5;
    // texture.rgb and vertexColor deliberately ignored
    vec4 color = vec4(gs, gs, gs, texture(Sampler0, texCoord0).a) * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
