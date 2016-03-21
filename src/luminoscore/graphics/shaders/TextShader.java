package luminoscore.graphics.shaders;

import luminoscore.util.math.vector.Vector2f;
import luminoscore.util.math.vector.Vector3f;

public class TextShader extends ShaderProgram {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 */
	
	private int location_color;
	private int location_translation;

	public TextShader(Shader shader) {
		super(shader);
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
	}
	
	public void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}
	
	public void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}

}
