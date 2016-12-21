package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;


/**
 * 
 * Text Shader for Text Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class TextShader extends ShaderProgram {
	
	private int location_color;
	private int location_translation;
	private int location_font;
	
	public static String VERT = "text.vert";
	public static String FRAG = "text.frag";
	
	/**
	 * Constructor
	 */
	public TextShader() {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
		location_font = super.getUniformLocation("font");
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
		super.bindAttribute(TEXTURES, "textureCoords");
	}
	
	/**
	 * Loads color of text to shader
	 * 
	 * @param color	Color of text
	 */
	public void loadColor(Vector3f color) {
		super.loadVector3f(location_color, color);
	}
	
	/**
	 * Loads position of text to shader
	 * 
	 * @param translation	Position of text
	 */
	public void loadTranslation(Vector2f translation) {
		super.loadVector2f(location_translation, translation);
	}
	
	/**
	 * Loads font ID to shader
	 * 
	 * @param font	Font GPU ID
	 */
	public void loadFont(float font) {
		super.loadFloat(location_font, font);
	}

}
