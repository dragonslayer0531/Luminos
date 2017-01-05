package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;


/**
 * 
 * Text Shader for Text Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class TextShader extends ShaderProgram {
	
	public static String VERT = "text.vert";
	public static String FRAG = "text.frag";
	
	/**
	 * Constructor
	 * @throws Exception 
	 */
	public TextShader() throws Exception {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		createUniform("color");
		createUniform("translation");
		createUniform("font");
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
		setUniform(getLocation("color"), color);
	}
	
	/**
	 * Loads position of text to shader
	 * 
	 * @param translation	Position of text
	 */
	public void loadTranslation(Vector2f translation) {
		setUniform(getLocation("translation"), translation);
	}
	
	/**
	 * Loads font ID to shader
	 * 
	 * @param font	Font GPU ID
	 */
	public void loadFont(float font) {
		setUniform(getLocation("font"), font);
	}

}
