layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec4 color;

uniform mat4 proj;

out vec2 frag_uv;
out vec4 frag_color;

void main(void) {
	frag_uv = texCoord;
	frag_color = color;
	gl_Position = proj * vec4(position.x, position.y, 0, 1);
}