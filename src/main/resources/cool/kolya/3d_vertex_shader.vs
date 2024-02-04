#version 330 core

layout (location = 0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 elementMatrix;
uniform mat4 cameraMatrix;

void main()
{
    gl_Position = projectionMatrix * cameraMatrix * elementMatrix * vec4(position, 1.0);
}