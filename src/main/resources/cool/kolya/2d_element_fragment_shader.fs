#version 330 core

in vec2 outTexCord;

uniform vec4 incolor;
uniform bool texture;
uniform sampler2D sampler;

out vec4 color;

void main()
{
    if (!texture) {
	    color = incolor;
	} else {
	    color = texture(sampler, outTexCord) * incolor;
	}
}