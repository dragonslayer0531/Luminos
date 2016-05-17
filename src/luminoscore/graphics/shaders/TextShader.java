package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.GlobalLock;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * Text Shader for Text Renderer
 *
 */
public class TextShader extends ShaderProgram {
	
	private int location_color;
	private int location_translation;
	private int location_font;
	
	public static String VERT = "text.vert";
	public static String FRAG = "text.frag";
	
	/**
	 * @param VERT	Vertex shader file
	 * @param FRAG	Fragment shader file
	 * 
	 * Constructor
	 */
	public TextShader() {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
		location_font = super.getUniformLocation("font");
	}
	
	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
		super.bindAttribute(GlobalLock.TEXTURES, "textureCoords");
	}
	
	/**
	 * @param color	Color of text
	 * 
	 * Loads color of text to shader
	 */
	public void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}
	
	/**
	 * @param translation	Position of text
	 * 
	 * Loads position of text to shader
	 */
	public void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}
	
	/**
	 * @param font	Font GPU ID
	 * 
	 * Loads font ID to shader
	 */
	public void loadFont(float font) {
		super.loadFloat(location_font, font);
	}

}
