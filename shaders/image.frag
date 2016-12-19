#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D texture2D;

void main(void) {

	out_Color = texture(texture2D, textureCoords);
	
}