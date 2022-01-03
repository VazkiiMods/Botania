#version 120

uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

uniform int startX; // Passed in, see BossBarHandler.java
uniform int startY; // Passed in, see BossBarHandler.java

uniform float grainIntensity; // Passed in via Callback
uniform float hpFract;

float rand(vec2 co) {
   return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {   
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);

    float r = color.r;
    float g = color.g;
    float b = color.b;
    
    float s = sin((texcoord.y + 0.0375) * 100.0 + sin(texcoord.x * 90.0 - time / 2.0) * hpFract) * 0.5 + 0.5;
    s = 1.0 - s;
    if(s <= 0.98)
        s = 0;
    s *= 0.4;
    
    r = min(1, r + s);
    g = min(1, g + s);
    b = min(1, b + s);
    
    if(hpFract <= 0.2) {
        float flash = (sin(time * 2.0) - 0.8) * 5;
        if(flash > 0) {
            g = max(0, g - flash);
            b = max(0, b - flash);
        }   
    }
    
    if(grainIntensity > 0) {
        float gs = rand(vec2(texcoord.x + time, texcoord.y));
        r = r * (1 - grainIntensity) + gs * grainIntensity;
        g = g * (1 - grainIntensity) + gs * grainIntensity;
        b = b * (1 - grainIntensity) + gs * grainIntensity;    
    }
    
    gl_FragColor = vec4(r, g, b, color.a);
}