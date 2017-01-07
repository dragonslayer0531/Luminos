package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

/**
 * 
 * Particle Shader for Particle Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class ParticleShader extends ShaderProgram {
	
	public static String VERT = "particle.vert";
	public static String FRAG = "particle.frag";

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public ParticleShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		createUniform("modelViewMatrix");
		createUniform("projectionMatrix");
		createUniform("texOffset1");
		createUniform("texOffset2");
		createUniform("texCoordInfo");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}
	
}
