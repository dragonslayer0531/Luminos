layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoordinates;
layout (location = 2) in vec3 normal;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[_MAX_LIGHTS_];

uniform int useFakeLighting;
uniform float density;
uniform float gradient;
uniform int numberOfRows;
uniform int maxLights;
uniform vec2 offset;
uniform vec4 plane;

uniform PointLight pointLights[_MAX_LIGHTS_];

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector[_MAX_LIGHTS_];
out vec3 toCameraVector;
out float visibility;
out PointLight pass_PointLights[_MAX_LIGHTS_];

const float shadowDistance = 100;
const float transitionDistance = 30.0;

void main(void) {

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoordinates = (textureCoordinates / numberOfRows) + offset;
	
	vec3 actualNormal = normal;
	if(useFakeLighting > 0.5){
		actualNormal = vec3(0.0,1.0,0.0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal,0.0)).xyz;
	for(int i = 0; i < _MAX_LIGHTS_; i++){
		toLightVector[i] = pointLights[i].position - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.0);
	
	distance = distance - (shadowDistance - transitionDistance);
	distance = distance / transitionDistance;
	for (int i = 0; i < _MAX_LIGHTS_; i++) {
		pass_PointLights[i].attenuation = pointLights[i].attenuation;
		pass_PointLights[i].color = pointLights[i].color;
	}
	
}