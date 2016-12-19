#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[20];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_Color;

uniform vec3 lightColor[20];
uniform vec3 attenuation[20];
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;
uniform int maxLights;

uniform sampler2D modelTexture;
uniform sampler2D shadowMap;

const int pcfCount = 7;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void) {

	float mapSize = 2048 * 4;
	float texelSize = 1.0 / mapSize;
	float total = 0.0;
	
	for(int x=-pcfCount; x<=pcfCount; x++){
		for(int y=-pcfCount; y<=pcfCount; y++){
			float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
			if(shadowCoords.z > objectNearestLight){
				total += 1.0;
			}
		}
	}
	
	total /= totalTexels;
	
	float lightFactor = 1.0 - (total * shadowCoords.w);

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0 ; i < maxLights; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
	}
	totalDiffuse = max(totalDiffuse * lightFactor, 0.2);
	
	vec4 textureColor = texture(modelTexture,pass_textureCoordinates);
	if(textureColor.a < 0.5){
		discard;
	}
	
	out_Color = textureColor * .5;
	
	vec4 rem =  vec4(totalDiffuse,1.0) * textureColor + vec4(totalSpecular,1.0);
	rem = mix(vec4(skyColor,1.0),out_Color, visibility);
	
	out_Color = out_Color + (rem * .25);
}