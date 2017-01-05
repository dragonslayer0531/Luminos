package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.graphics.shaders.postprocess.PostProcess;

/**
 * 
 * Creates Image Shader for Image Renderer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ImageShader extends ShaderProgram implements PostProcess {

	public static String vertexFile = "image.vert";
	public static String fragmentFile = "image.frag";
	
	/**
	 * Constructor
	 * @throws Exception 
	 */
	public ImageShader() throws Exception {
		super(vertexFile, fragmentFile);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}

}
