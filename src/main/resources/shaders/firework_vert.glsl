#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in float phi;
layout(location = 2) in float theta;

out vec4 passColor;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float radius;
uniform vec3 color;
uniform float rFactor;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0) + vec4(rFactor * radius * cos(theta) * sin(phi), rFactor * radius * sin(theta) * sin(phi), rFactor * radius * cos(phi), 0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    passColor = vec4(color, 1 - smoothstep(0, 1, radius * 0.1));
}
