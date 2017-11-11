#version 120

uniform sampler2D bgl_RenderedTexture;
uniform int time; // Passed in, see ShaderHelper.java

// Source: http://wp.applesandoranges.eu/?p=14 (Modified)
void main() {
    vec4 color = vec4(0);
    vec2 texcoord = vec2(gl_TexCoord[0]);

    for(int i = -4 ; i < 4; i++)
        for(int j = -3; j < 3; j++)
            color += texture2D(bgl_RenderedTexture, texcoord + vec2(j, i));

    float brightness = sin(time / 20.0) * 0.5 + 0.15;
    float alpha = min(1, sin(time / 30.0) * 0.5 + 0.75);
    
    gl_FragColor = vec4((color * color * 0.0005 * brightness + texture2D(bgl_RenderedTexture, texcoord)).rgb, alpha * gl_Color.a);
}
