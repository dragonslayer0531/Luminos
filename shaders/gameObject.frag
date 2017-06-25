in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[_MAX_LIGHTS_];
in PointLight pass_PointLights[_MAX_LIGHTS_];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform DirectionalLight sun;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform int numPointLights;

void main(void){

	vec4 textureColor = texture(modelTexture,pass_textureCoordinates);
	if(textureColor.a < 0.5) {
		discard;
	}
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < numPointLights;i++) {
		float distance = length(toLightVector[i]);
		float attFactor = pass_PointLights[i].attenuation.x + pass_PointLights[i].attenuation.y * distance + pass_PointLights[i].attenuation.z * distance * distance;
				
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl, 0.2);
		totalDiffuse = totalDiffuse + (brightness * pass_PointLights[i].color)/attFactor;
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * pass_PointLights[i].color) / attFactor;
	}
				
	vec3 unitLightVector = normalize(sun.direction);
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl, 0.2);
	totalDiffuse = totalDiffuse + (brightness * sun.color) * sun.intensity;
		
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor,shineDamper);
	totalSpecular = totalSpecular + (dampedFactor * reflectivity * sun.color) / sun.intensity;
	
	totalDiffuse = max(totalDiffuse, 0.2);
	
	out_Color =  vec4(totalDiffuse,1.0) * textureColor + vec4(totalSpecular,1.0);
	out_Color =  mix(vec4(skyColor, 1.0), out_Color, visibility);

}