#version 150
// [VanillaCopy] position_color_tex_lightmap.fsh, changes noted

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

uniform float GameTime;
void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    // Botania - adjust color
    // GameTime is in [0, 1) over course of the day, turn it back into ticks
    float time = GameTime * 24000;
    float c = sin(time / 80.0 + sin(texCoord0.x / 5 + (texCoord0.y - cos(time / 300.0) * 0.25) / ((1 - sin(time / 200.0) / 10.0) * 5)) * sin(time / 600.0) * 1000) * 0.5 + 0.5;
    float g = 1 - c * 0.2 - 0.2;
    float b = 1 - c * 0.2;
    fragColor = vec4(0, g, b, color.a) * ColorModulator;
}
