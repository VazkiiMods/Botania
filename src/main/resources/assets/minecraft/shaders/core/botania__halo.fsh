#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float GameTime;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    float timeTicks = GameTime * 24000;
    float brightness = sin(timeTicks / 20.0) * 0.5 + 0.5;

    vec4 sum = vec4(0);
    for(int i = -4 ; i < 4; i++) {
        for(int j = -3; j < 3; j++) {
            sum += texture(Sampler0, texCoord0 + vec2(j, i)) * 0.25;
        }
    }
    vec4 sampled = texture(Sampler0, texCoord0);
    vec4 color = vec4(0);
    if(sampled.r < 0.3) {
        color = sum * sum * 0.012 + sampled;
    } else {
        if(sampled.r < 0.5) {
            color = sum * sum * 0.009 * brightness + sampled;
        } else {
            color = sum * sum * 0.0075 * brightness + sampled;
        }
    }

    color *= vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = color * ColorModulator;
}
