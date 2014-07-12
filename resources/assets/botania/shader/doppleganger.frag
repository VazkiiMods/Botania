in vec2 texcoord;
uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

float rand(vec2 co) {
   return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    
    float gs = (color.r + color.g + color.b) / 50.0;
    
    vec2 seed = texcoord.xy * time;
    float m = 0.05;
    
    float r = gs + rand(texcoord) * m;
    float g = gs + rand(texcoord) * m;
    float b = gs + rand(texcoord) * m;
    
    gl_FragColor = vec4(r, g, b, color.a);
}