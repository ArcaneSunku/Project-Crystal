#version 440 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec2 aTextureCoords;

uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;

out vec2 fTextureCoords;

void main()
{
    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aPosition, 1.0);
    fTextureCoords = aTextureCoords;
}