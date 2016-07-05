#version 400 core

in vec2 pass_textureCoordinates;
in int pass_maxLights;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVector[20];
in vec4 shadowCoords;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;

uniform int tileFactor;

uniform vec3 lightColor[20];
uniform vec3 attenuation[20];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform int maxLights;


void main(void){

	float objectNearestLight = texture(shadowMap, shadowCoords.xy).r;
	float lightFactor = 1.0;
	if(shadowCoords.z > objectNearestLight) {
		lightFactor = .6;
	}

	vec4 blendMapColor = texture(blendMap, pass_textureCoordinates);
	
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_textureCoordinates * tileFactor;
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture,tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture,tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture,tiledCoords) * blendMapColor.b;
	
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < maxLights; i++){
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

	out_Color = totalColor * .5;
	totalDiffuse = max(totalDiffuse, 0.4) * lightFactor;
	vec4 rem = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
	rem = mix(vec4(skyColor, 1.0), out_Color, visibility);
	
	out_Color = out_Color + rem;
	out_Color.w = 1;
}