in vec2 textureCoords;

layout (location = 0) out vec4 out_Color;

uniform sampler2D texture2D;

void main(void) {

	out_Color = texture(texture2D, textureCoords);
	
}