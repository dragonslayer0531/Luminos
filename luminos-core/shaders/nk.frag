precision mediump float;

in vec2 frag_uv;
in vec4 frag_color;

uniform sampler2D tex;

out vec4 color;

void main(void) {
	color = frag_color * texture(tex, frag_uv.st);
}