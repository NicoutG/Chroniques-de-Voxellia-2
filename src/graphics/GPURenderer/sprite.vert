#version 120

varying vec2 vTex;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    vTex = gl_MultiTexCoord0.st;
}
