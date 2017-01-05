package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.Maths;

/**
 * 
 * Water Shader for Water Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class WaterShader extends ShaderProgram {

	public static String VERT = "water.vert";
	public static String FRAG = "water.frag";

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public WaterShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		bindAttribute(POSITION, "position");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("modelMatrix");
		createUniform("reflectionTexture");
		createUniform("refractionTexture");
		createUniform("dudvMap");
		createUniform("normalMap");
		createUniform("moveFactor");
		createUniform("cameraPosition");
		createUniform("lightPosition");
		createUniform("lightColor");
		createUniform("depthMap");
		createUniform("near");
		createUniform("far");
		createUniform("tiling");
		createUniform("waveStrength");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("skyColor");
	}

	/**
	 * Connect texture units
	 */
	public void connectTextureUnits() {
		setUniform(getLocation("reflectionTexture"), 0);
		setUniform(getLocation("refractionTexture"), 1);
		setUniform(getLocation("dudvMap"), 2);
		setUniform(getLocation("normalMap"), 3);
		setUniform(getLocation("depthMap"), 4);
	}
	
	public void loadRenderBox(float nearPlane, float farPlane) {
		setUniform(getLocation("near"), nearPlane);
		setUniform(getLocation("far"), farPlane);
	}
	
	/**
	 * Loads {@link PointLight} to shader
	 * 
	 * @param sun	Focal light of scene 
	 */
	public void loadPointLight(PointLight sun) {
		setUniform(getLocation("lightColor"), sun.getColor());
		setUniform(getLocation("lightPosition"), sun.getPosition());
	}

	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		setUniform(getLocation("projectionMatrix"), projection);
	}
	
	/**
	 * Loads movement factor of water
	 * 
	 * @param factor	Movement factor of water
	 */
	public void loadMoveFactor(float factor) {
		setUniform(getLocation("moveFactor"), factor);
	}

	/**
	 * Load view matrix to shader
	 * 
	 * @param camera	Camera to calculate view matrix of
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		setUniform(getLocation("viewMatrix"), viewMatrix);
		setUniform(getLocation("cameraPosition"), camera.getPosition());
	}

	/**
	 * Loads model matrix to shader
	 * 
	 * @param modelMatrix	Model matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix){
		setUniform(getLocation("modelMatrix"), modelMatrix);
	}
	
	/**
	 * Loads tiling amount to shader
	 * 
	 * @param tiling		amount to tile per quad
	 */
	public void loadTiling(float tiling) {
		setUniform(getLocation("tiling"), tiling);
	}
	
	/**
	 * Loads wave strength to shader
	 * 
	 * @param waveStrength		strength of waves
	 */
	public void loadWaveStrength(float waveStrength) {
		setUniform(getLocation("waveStrength"), waveStrength);
	}
	
	/**
	 * Loads shine damper to shader
	 * 
	 * @param shineDamper	amount of shine damper
	 */
	public void loadShineDamper(float shineDamper) {
		setUniform(getLocation("shineDamper"), shineDamper);
	}
	
	/**
	 * Loads reflection amount
	 * 
	 * @param reflection	percentage of reflection 
	 */
	public void loadReflectivity(float reflection) {
		setUniform(getLocation("reflectivity"), reflection);
	}
	
	public void loadSkyColor(Vector3f skyColor) {
		setUniform(getLocation("skyColor"), skyColor);
	}

}
