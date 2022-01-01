#version 330 core

in vec3 passColor;
in vec3 passNormal;
in vec3 toLight;
in vec3 toCamera;

out vec4 fragColor;

uniform vec3 lightColor;
uniform int isLightSource;

const float ambientStrength = 0.2;
const float diffuseStrength = 1.0;
const float specularStrength = 0.0;
const float phongExponent = 0.0;

void main() {
    vec4 color = vec4(passColor, 1.0);

    // phong
    vec3 unitNormal = normalize(passNormal);
    vec3 unitToCamera = normalize(toCamera);
    vec3 unitToLight = normalize(toLight);
    if (isLightSource != 0) {
        unitToLight = -unitToLight;
    }
    vec3 reflectedLightDirection = reflect(-unitToLight, unitNormal);

    float totalAmbient = ambientStrength;
    float totalDiffuse = diffuseStrength * max(dot(unitNormal, unitToLight), 0.0);
    float totalSpecular = specularStrength * pow(max(dot(reflectedLightDirection, unitToCamera), 0.0), phongExponent);

    fragColor = (totalAmbient + totalDiffuse) * vec4(lightColor, 1.0) * color + totalSpecular * vec4(lightColor, 1.0);
}