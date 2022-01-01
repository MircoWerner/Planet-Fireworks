#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec3 normal;

out vec3 passColor;
out vec3 passNormal;
out vec3 toLight;
out vec3 toCamera;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    passColor = color;

    passNormal = (transpose(inverse(viewMatrix * transformationMatrix)) * vec4(normal, 0.0)).xyz;
    toLight = (viewMatrix * vec4(lightPosition, 1.0)).xyz - positionRelativeToCam.xyz;
    toCamera = -positionRelativeToCam.xyz;
}
