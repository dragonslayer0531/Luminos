package com.luminos.graphics.shaders.postprocess;

import com.luminos.graphics.shaders.ShaderProgram;

/**
 * 
 * Contrast shader to use in Post Processing pipeline
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class ContrastShader extends ShaderProgram implements PostProcess {
	
	public static String VERT = "contrast.vert";
	public static String FRAG = "contrast.frag";
	
	/**
	 * Constructor
	 * @throws Exception 
	 */
	public ContrastShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	public void getAllUniformLocations() {
		createUniform("contrast");
	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	public void bindAttributes() {

	}

}
