in vec2 textureCoords;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform sampler2D texture2D;

void main(void) {

	out_Color = texture(texture2D, textureCoords);
	out_BrightColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	
}