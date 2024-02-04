#version 330 core

layout (location =0) in vec2 position;

uniform mat4 rotate;
uniform mat4 scale;
uniform mat4 translate;

void main()
{
    gl_Position = translate * rotate * scale * vec4(position, 1.0, 1.0);
}