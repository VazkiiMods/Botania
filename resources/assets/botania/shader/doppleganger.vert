#version 120

varying vec2 texcoord;
uniform int time; // Passed in, see ShaderHelper.java

uniform float disfiguration; // Passed in via Callback

float rand(vec2 co) {
   return (fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453) - 0.5) * 2;
}

void main() {
    vec4 vert = gl_Vertex;
    float seed = rand(vec2(time, time));

    vert.x = vert.x + rand(vec2(vert.y * seed, vert.z * seed)) * disfiguration;
    vert.y = vert.y + rand(vec2(vert.x * seed, vert.z * seed)) * disfiguration;
    vert.z = vert.z + rand(vec2(vert.x * seed, vert.y * seed)) * disfiguration;
    
    gl_Position = gl_ModelViewProjectionMatrix * vert;
    texcoord = vec2(gl_MultiTexCoord0);
}