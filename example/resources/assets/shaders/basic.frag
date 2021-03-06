#version 330 core

in vec2 fTextureCoords;

uniform sampler2D uTextureSampler;
uniform vec3 uColor;
uniform int uUseTexture;

out vec4 oColor;

void main()
{
    if(uUseTexture == 0)
    {
        oColor = vec4(uColor, 1.0);
    }
    else
    {
        oColor = vec4(uColor, 1.0) * texture(uTextureSampler, fTextureCoords);
    }
}