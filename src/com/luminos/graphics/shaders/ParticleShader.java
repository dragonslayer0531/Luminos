package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;

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
	
	/**
	 * Loads texture coordinates to shader
	 * 
	 * @param offset1	Top left
	 * @param offset2	Bottom right
	 * @param numRows	Rows in the shader
	 * @param blend		Blend factor
	 */
	public void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend) {
		setUniform(getLocation("texOffset1"), offset1);
		setUniform(getLocation("location_texOffset2"), offset2);
		setUniform(getLocation("texCoordInfo"), new Vector2f(numRows, blend));
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}
	
	/**
	 * Loads Model View Matrix to shader
	 * 
	 * @param matrix	Model View Matrix
	 */
	public void loadModelViewMatrix(Matrix4f matrix) {
		setUniform(getLocation("modelViewMatrix"), matrix);
	}
	
	/**
	 * Loads Projection Matrix to shader
	 * 
	 * @param matrix	Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		setUniform(getLocation("projectionMatrix"), matrix);
	}

}
