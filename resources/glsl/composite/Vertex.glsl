#version 450

layout (location = 0) in vec2 position;

out vec2 vert_position;

void main() {
    vert_position = position;
    gl_Position = vec4(position, 0.0, 1.0);
}
