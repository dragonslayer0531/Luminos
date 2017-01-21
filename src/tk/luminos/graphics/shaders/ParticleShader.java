package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.POSITION;

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
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public ParticleShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
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
