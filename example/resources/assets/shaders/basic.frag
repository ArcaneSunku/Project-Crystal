#version 440 core

in vec2 fTextureCoords;

uniform sampler2D uTextureSampler;
uniform int uUseTexture;

out vec4 oColor;

void main()
{
    if(uUseTexture == 0)
    {
        oColor = vec4(0.0, 0.5, 0.5, 1.0);
    }
    else
    {
        oColor = texture(uTextureSampler, fTextureCoords);
    }
}