#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;
uniform float font;

void main(void) {

	float width = 0.4 + font * 0.02;
	float edge = .1;
	if(font < 5) {
		edge = .1 + font * 0.01;
	} else {
		edge = .1 - font * 0.01;
	}
	float distance = 1 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1 - smoothstep(width, width + edge, distance);

	out_color = vec4(color, alpha);
	
}