layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normal;

out vec2 pass_textureCoords;

uniform mat4 projModelMatrix;

void main(void) {
	gl_Position = projModelMatrix * vec4(position, 1.0);
	pass_textureCoords = textureCoords;
}