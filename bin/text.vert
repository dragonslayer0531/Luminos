in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform vec2 translation;

void main(void) {
	gl_Position = vec4(position, 0, 1);
	pass_textureCoords = textureCoords;
}