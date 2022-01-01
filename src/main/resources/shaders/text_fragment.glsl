#version 330 core

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 color;

void main()
{
    fragColor = vec4(color, texture(texture_sampler, outTexCoord).a);
}