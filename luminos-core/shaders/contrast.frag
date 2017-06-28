in vec2 textureCoords;

out vec4 out_Color; 

uniform sampler2D colorTexture;
uniform float contrast;

void main(void) {

	out_Color = texture(colorTexture, textureCoords);
	out_Color.rgb = (out_Color.rgb - 0.5) * (1.0 + contrast) + 0.5;
	
}