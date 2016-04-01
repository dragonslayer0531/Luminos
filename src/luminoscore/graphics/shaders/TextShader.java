package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextShader extends ShaderProgram {
	
	private int location_color;
	private int location_translation;
	private int location_font;
	
	public TextShader(String VERT, String FRAG) {
		super(VERT, FRAG);
	}
	
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
		location_font = super.getUniformLocation("font");
	}
	
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}
	
	public void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}
	
	public void loadFont(float font) {
		super.loadFloat(location_font, font);
	}

}
