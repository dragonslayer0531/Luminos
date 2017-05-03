in float visibility;
in PointLight pass_PointLights[_MAX_LIGHTS_];
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVector[_MAX_LIGHTS_];
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;
uniform DirectionalLight sun;
uniform int tileFactor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform int numPointLights;

uniform int pcfCount;
float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void){
	
	float total = 0.0;
	
	for(int x=-pcfCount; x<=pcfCount; x++){
		for(int y=-pcfCount; y<=pcfCount; y++){
			float textureDepth = texture(shadowMap, shadowCoords.xy + vec2(x, y) * 1 / textureSize(shadowMap, 0)).r;
			total += (shadowCoords.z - 0.01 > textureDepth) ? 1 : 0;
		}
	}
	
	total *= sun.intensity;
	total /= totalTexels;
	
	float lightFactor = 1.0 - (total * shadowCoords.w);

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
	
	for(int i = 0; i < numPointLights; i++){
		float distance = length(toLightVector[i]);
		float attFactor = pass_PointLights[i].attenuation.x + (pass_PointLights[i].attenuation.y * distance) + (pass_PointLights[i].attenuation.z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalDiffuse = totalDiffuse + (brightness * pass_PointLights[i].color)/attFactor + 0.1;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * pass_PointLights[i].color)/attFactor;
	}
	
	vec3 unitLightVector = normalize(sun.direction);	
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl,0.0);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);
	totalDiffuse = totalDiffuse + (brightness * sun.color) * sun.intensity + 0.1;
	totalSpecular = totalSpecular + (dampedFactor * reflectivity * sun.color) * sun.intensity;

	totalDiffuse = max(totalDiffuse * lightFactor, 0.2);

	out_Color =  vec4(totalDiffuse,1.0) * totalColor + vec4(totalSpecular,1.0);
	out_Color = mix(vec4(skyColor,1.0),out_Color, visibility);
}