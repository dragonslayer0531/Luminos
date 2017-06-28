in vec2 pass_textureCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;

void main(void) {
	
	out_Color = colour * texture(fontAtlas, pass_textureCoords);
	
}