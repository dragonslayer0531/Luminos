package tk.luminos.luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import tk.luminos.luminoscore.GlobalLock;

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
	
	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	private int location_texOffset1;
	private int location_texOffset2;
	private int location_texCoordInfo;

	/**
	 * Constructor
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
		location_texOffset1 = super.getUniformLocation("texOffset1");
		location_texOffset2 = super.getUniformLocation("texOffset2");
		location_texCoordInfo = super.getUniformLocation("texCoordInfo");
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
		super.load2DVector(location_texOffset1, offset1);
		super.load2DVector(location_texOffset2, offset2);
		super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
	}
	
	/**
	 * Loads Model View Matrix to shader
	 * 
	 * @param matrix	Model View Matrix
	 */
	public void loadModelViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_modelViewMatrix, matrix);
	}
	
	/**
	 * Loads Projection Matrix to shader
	 * 
	 * @param matrix	Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

}
