in vec2 textureCoords;

out vec4 out_Color;

uniform vec3 color = vec3(1, 1, 1);

uniform sampler2D alphaMask;

void main(void){

	vec4 tex = texture(alphaMask, textureCoords);
	out_Color = vec4(color.rgb, tex.w);

}