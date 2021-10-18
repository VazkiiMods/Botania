#version 150

#moj_import <fog.glsl>

// [VanillaCopy] rendertype_entity_translucent.fsh, changes noted

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

uniform float BotaniaGrainIntensity;

float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    // Botania - Grayscale + Noise with grain intensity
    float r = rand(texCoord0);
    vec3 offset = BotaniaGrainIntensity * vec3(r, r, r);
    float gs = (color.r + color.g + color.b) / 50.0;
    color = vec4(vec3(gs, gs, gs) + offset, color.a);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
