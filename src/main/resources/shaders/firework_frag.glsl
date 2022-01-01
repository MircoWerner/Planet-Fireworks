#version 330 core

in vec4 passColor;

out vec4 fragColor;

void main() {
    fragColor = passColor;
}