in vec2 pass_textureCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;
uniform float font;

void main(void) {

	float width = 0.5;
	float edge = 0.1;
	
	width = width + (0.01 * (font - 3));
	edge = edge + (0.02 * (3 - font)) * abs(0.02 * (3 - font));
	
	float distance = texture(fontAtlas, pass_textureCoords).a;
	float alpha = smoothstep(width, width + edge, distance);
	
	out_Color = vec4(color, alpha);
	
}