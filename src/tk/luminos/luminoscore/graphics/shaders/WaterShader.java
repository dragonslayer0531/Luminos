package tk.luminos.luminoscore.graphics.shaders;

import static tk.luminos.luminoscore.ConfigData.POSITION;

import org.lwjgl.util.vector.Matrix4f;

import tk.luminos.luminoscore.graphics.gameobjects.Camera;
import tk.luminos.luminoscore.graphics.gameobjects.Light;
import tk.luminos.luminoscore.tools.Maths;

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

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvMap;
	private int location_normalMap;
	private int location_moveFactor;
	private int location_cameraPosition;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_depthMap;
	private int location_nearPlane;
	private int location_farPlane;
	private int location_tiling;
	private int location_waveStrength;
	private int location_shineDamper;
	private int location_reflectivity;

	/**
	 * Constructor
	 */
	public WaterShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		bindAttribute(POSITION, "position");
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
		location_dudvMap = getUniformLocation("dudvMap");
		location_normalMap = getUniformLocation("normalMap");
		location_moveFactor = getUniformLocation("moveFactor");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_depthMap = getUniformLocation("depthMap");
		location_nearPlane = getUniformLocation("near");
		location_farPlane = getUniformLocation("far");
		location_tiling = getUniformLocation("tiling");
		location_waveStrength = getUniformLocation("waveStrength");
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
	}

	/**
	 * Connect texture units
	 */
	public void connectTextureUnits() {
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_dudvMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_depthMap, 4);
	}
	
	public void loadRenderBox(float nearPlane, float farPlane) {
		super.loadFloat(location_nearPlane, nearPlane);
		super.loadFloat(location_farPlane, farPlane);
	}
	
	/**
	 * Loads {@link Light} to shader
	 * 
	 * @param sun	Focal light of scene 
	 */
	public void loadLight(Light sun) {
		super.loadVector(location_lightColor, sun.getColor());
		super.loadVector(location_lightPosition, sun.getPosition());
	}

	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Loads movement factor of water
	 * 
	 * @param factor	Movement factor of water
	 */
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}

	/**
	 * Load view matrix to shader
	 * 
	 * @param camera	Camera to calculate view matrix of
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	/**
	 * Loads model matrix to shader
	 * 
	 * @param modelMatrix	Model matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}
	
	/**
	 * Loads tiling amount to shader
	 * 
	 * @param tiling		amount to tile per quad
	 */
	public void loadTiling(float tiling) {
		super.loadFloat(location_tiling, tiling);
	}
	
	/**
	 * Loads wave strength to shader
	 * 
	 * @param waveStrength		strength of waves
	 */
	public void loadWaveStrength(float waveStrength) {
		super.loadFloat(location_waveStrength, waveStrength);
	}
	
	/**
	 * Loads shine damper to shader
	 * 
	 * @param shineDamper	amount of shine damper
	 */
	public void loadShineDamper(float shineDamper) {
		super.loadFloat(location_shineDamper, shineDamper);
	}
	
	/**
	 * Loads reflection amount
	 * 
	 * @param reflection	percentage of reflection 
	 */
	public void loadReflectivity(float reflection) {
		super.loadFloat(location_reflectivity, reflection);
	}

}
