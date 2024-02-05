layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 texCord;

uniform mat4 element;

out vec2 outTexCord;

void main() {
    gl_Position =  element * vec4(pos, 0.0, 1.0);
    outTexCord = texCord;
}