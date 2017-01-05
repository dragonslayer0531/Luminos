package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.tools.maths.matrix.Matrix4f;

/**
 * 
 * Gui Shader to use in GuiRenderer
 *
 * @author Nick Clark
 * @version 1.1
 */

public class GuiShader extends ShaderProgram {
	
	public static String VERT = "gui.vert";
	public static String FRAG = "gui.frag";

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public GuiShader() throws Exception {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		createUniform("transformationMatrix");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}
	
	/**
	 * Load transformation matrix to shader
	 * 
	 * @param matrix	Transformation matrix
	 */
	public void loadTransformation(Matrix4f matrix){
		setUniform(getLocation("transformationMatrix"), matrix);
	}


}
