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
uniform float near;
uniform float far;
uniform float waveStrength;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

	//Calculate Reflection and Refraction texture coordinates
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x,ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x,-ndc.y);

	//Calculate water depth
    float depth = texture(depthMap, refractTexCoords).r;
    float floorDistance = 2.0*near*far/(far+near-(2.0*depth-1.0)*(far-near));
    depth = gl_FragCoord.z;
    float waterDistance = 2.0*near*far/(far+near-(2.0*depth-1.0)*(far-near));
    float waterDepth = floorDistance - waterDistance;
    
    //Calculate Texture Distortion
    vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x+moveFactor,textureCoords.y)).rg*0.1;
    distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y+moveFactor);
    vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth/5.0, 0, 1);
    
    refractTexCoords += totalDistortion;
    refractTexCoords = clamp(refractTexCoords,0.001,0.999);
    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(reflectTexCoords.x,0.001,0.999);
    reflectTexCoords.y = clamp(reflectTexCoords.y,-0.999,-0.001);
    
    vec4 reflectColour = texture(reflectionTexture, reflectTexCoords);
    vec4 refractColour = texture(refractionTexture, refractTexCoords);
    
    vec4 normalMapColour = texture(normalMap, distortedTexCoords);
    vec3 normal = vec3(normalMapColour.r * 2.0 - 1.0, normalMapColour.b*3.0, normalMapColour.g * 2.0 - 1.0);
    normal = normalize(normal);
    
    //fresnel effect
    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);

    vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth/5.0, 0, 1);

	out_Color = mix(reflectColour, refractColour, refractiveFactor);
    out_Color = mix(out_Color, vec4(0,0.3,0.5,1), 0.15) + vec4(specularHighlights, 0.0);
    float depth_factor = clamp((depth - 10) / 15, 0, 1);
    out_Color = mix(out_Color, vec4(205/255, 133/255, 63/255, 1), depth_factor);
    out_Color.a = clamp(waterDepth/20.0, 0, 1);
 
}
