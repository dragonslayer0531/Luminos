layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoordinates;
layout (location = 2) in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector[_MAX_LIGHTS_];
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoords;
out PointLight pass_PointLights[_MAX_LIGHTS_];

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[_MAX_LIGHTS_];
uniform PointLight pointLights[_MAX_LIGHTS_];

uniform mat4 toShadowMapSpace;

uniform float density;
uniform float gradient;

uniform float shadowDistance;
uniform float transitionDistance;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	shadowCoords = toShadowMapSpace * worldPosition;
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	pass_textureCoordinates = textureCoordinates;
	
	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	
	for(int i = 0; i < _MAX_LIGHTS_; i++){
		toLightVector[i] = pointLights[i].position - worldPosition.xyz;
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
		
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.0);
	
	distance = distance - (shadowDistance - transitionDistance);
	distance = distance / transitionDistance;
	shadowCoords.w = clamp(1 - distance, 0, 1);
	
	for (int i = 0; i < _MAX_LIGHTS_; i++) {
		pass_PointLights[i].attenuation = pointLights[i].attenuation;
		pass_PointLights[i].color = pointLights[i].color;
	}

}