#version 400 core
 
in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;
 
out vec4 out_Color;
 
uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform vec3 lightColor;

uniform float moveFactor;

const float waveStrength =  0.04;
const float shineDamper = 20.0;
const float reflectivity = 0.5;
 
void main(void) {
 
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
    
    float near = 0.1;
    float far = 1000.0;
    float depth = texture(depthMap, refractTexCoords).r;
    float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    
    depth = gl_FragCoord.z;
    float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    float waterDepth = floorDistance - waterDistance;
            
    vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
    distortedTexCoords = textureCoords +  vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
    vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 2.0, 0.0, 1.0);
    
    refractTexCoords += totalDistortion;
    refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
    
    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(refractTexCoords.x, 0.001, 0.999);
    reflectTexCoords.y = clamp(refractTexCoords.y, -0.999, -0.001);
       
    vec4 reflectColour = texture(reflectionTexture, reflectTexCoords);
    vec4 refractColour = texture(refractionTexture, refractTexCoords);
     
 	vec4 normalMapColor = texture(normalMap, distortedTexCoords);
 	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
 	normal = normalize(normal);
 	
    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = pow(refractiveFactor, 1.5);
 	
 	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
 	float specular = max(dot(reflectedLight, viewVector), 0.0);
 	specular = pow(specular, shineDamper);
 	vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5.0, 0.0, 1.0);
 
    out_Color = mix(reflectColour, refractColour, refractiveFactor);
    out_Color = mix(out_Color, vec4(0, 0.3, 0.5, 1), 0.05) + vec4(specularHighlights, 0.0);
    out_Color.a = clamp(waterDepth / 5.0, 0.0, 1.0);
    
}