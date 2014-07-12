varying out vec2 texcoord;
uniform int time; // Passed in, see ShaderHelper.java

float rand(vec2 co) {
   return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec4 vert = gl_Vertex;
    float seed = rand(vec2(time, time));
    float m = 0.025;
    vert.x = vert.x + rand(vec2(vert.y * seed, vert.z * seed)) * m;
    vert.y = vert.y + rand(vec2(vert.x * seed, vert.z * seed)) * m;
    vert.z = vert.z + rand(vec2(vert.x * seed, vert.y * seed)) * m;
    
    gl_Position = gl_ModelViewProjectionMatrix * vert;
    texcoord = vec2(gl_MultiTexCoord0);
}