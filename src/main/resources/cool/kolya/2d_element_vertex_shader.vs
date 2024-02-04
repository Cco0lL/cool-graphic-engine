layout (location = 0) in vec2 pos;

uniform mat4 element;

void main() {
    gl_Position =  element * vec4(pos, 0.0, 1.0);
}