layout (location = 0) in vec2 position;

out vec2 textureCoords;

void main(void) {

	gl_Position = vec4(position, 0, 1);
	textureCoords = position * 0.5 + 0.5;

}