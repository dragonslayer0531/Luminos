#version 400

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct DirectionalLight
{
	vec3 color;
	vec3 direction;
	float intensity;
};

struct Material
{
    vec3 color;
    int useColor;
    float reflectance;
};

struct PointLight
{
    vec3 color;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

vec4 calcLightColor(vec3 color, float intensity, vec3 position, vec3 toLight, vec3 normal) {
	vec4 diffuse = vec4(0, 0, 0, 0);
	vec4 specular = vec4(0, 0, 0, 0);
	
	float diffuseFactor = max(dot(normal, toLight), 0.0);
	diffuse = vec4(color, 1.0) * intensity * diffuseFactor;
	
	vec3 cameraDir = normalize(-position);
	vec3 fromLight = -toLight;
	vec3 reflected = normalize(reflect(fromLight, normal));
	float specularFactor = max(dot(cameraDir, reflected), 0.0);
	specularFactor = pow(specularFactor, specularPower);
	specular = intensity * specularFactor * material.reflectance * vec4(color, 1.0);
	
	return (diffuse + specular);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 lightDirection = light.position - position;
    vec3 toLightDir  = normalize(lightDirection);
    vec4 lightColor = calcLightColor(light.color, light.intensity, position, toLightDir, normal);

    float distance = length(lightDirection);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;
    return lightColor / attenuationInv;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
    vec4 baseColor; 
    if ( material.useColor == 1 )
    {
        baseColor = vec4(material.color, 1);
    }
    else
    {
        baseColor = texture(texture_sampler, outTexCoord);
    }

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += calcDirectionalLight(directionalLight, mvVertexPos, mvVertexNormal);
    totalLight += calcPointLight(pointLight, mvVertexPos, mvVertexNormal);
    
    fragColor = baseColor * totalLight;
}