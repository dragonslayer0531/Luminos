layout (location = 0) in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector;

uniform float tiling;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

void main(void) {

	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
 	clipSpace = gl_Position;
 	textureCoords = vec2(position.x/2.0 + 0.5, position.y/2.0 + 0.5) * tiling;
 	toCameraVector = cameraPosition - worldPosition.xyz;
 	fromLightVector = worldPosition.xyz - lightPosition;
 
}