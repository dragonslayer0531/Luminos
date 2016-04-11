package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

import luminoscore.GlobalLock;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * Particle Shader for Particle Renderer
 *
 */

public class ParticleShader extends ShaderProgram {
	
	private static final String VERT = "res/shaders/particle.vert";
	private static final String FRAG = "res/shaders/particle.frag";
	
	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	/**
	 * Construcor
	 */
	public ParticleShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
	}
	
	/**
	 * @param matrix	Model View Matrix
	 * 
	 * Loads Model View Matrix to shader
	 */
	public void loadModelViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_modelViewMatrix, matrix);
	}
	
	/**
	 * @param matrix	Projection Matrix
	 * 
	 * Loads Projection Matrix to shader
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

}
