package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.NORMALS;
import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import java.util.List;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.Light;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;
import com.luminos.tools.Maths;

/**
 * 
 * Terrain Shader for Terrain Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class TerrainShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 20;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_shineDamper;
	private int location_attenuation[];
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_shadowMap;
	private int location_plane;
	private int location_toShadowMapSpace;
	private int location_density;
	private int location_gradient;
	private int location_maxLights;
	private int location_tileFactor;
	
	public static String VERT = "terrain.vert";
	public static String FRAG = "terrain.frag";

	/**
	 * Constructor
	 */
	public TerrainShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
		super.bindAttribute(TEXTURES, "textureCoordinates");
		super.bindAttribute(NORMALS, "normal");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_shadowMap = super.getUniformLocation("shadowMap");
		location_plane = super.getUniformLocation("plane");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i < MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_density = super.getUniformLocation("density");
		location_gradient = super.getUniformLocation("gradient");
		location_maxLights = super.getUniformLocation("maxLights");
		location_tileFactor = super.getUniformLocation("tileFactor");
	}
	
	/**
	 * Connect texture units
	 */
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
		super.loadInt(location_shadowMap, 5);
	}
	
	/**
	 * Loads shadow space matrix to shader
	 * 
	 * @param matrix	Shadow Space Matrix
	 */
	public void loadToShadowSpaceMatrix(Matrix4f matrix) {
		super.loadMatrix4f(location_toShadowMapSpace, matrix);
	}
	
	/**
	 * Loads sky color
	 * 
	 * @param skyColor		Sky color
	 */
	public void loadSkyColor(Vector3f skyColor){
		super.loadVector3f(location_skyColor, skyColor);
	}
	
	/**
	 * Loads shine values to shader
	 * 
	 * @param damper		Damper value
	 * @param reflectivity	Reflectivity value
	 */
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**
	 * Loads transformation matrix to shader
	 * 
	 * @param matrix	Transformation matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix4f(location_transformationMatrix, matrix);
	}
	
	/**
	 * Loads lights to shader
	 * 
	 * @param lights	List of {@link Light}s
	 */
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
				super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	/**
	 * Loads view matrix to shader
	 * 
	 * @param camera	Camera to load view matrix of
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix4f(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix4f(location_projectionMatrix, projection);
	}
	
	/**
	 * Loads clip plane to shader
	 * 
	 * @param clipPlane	Clipping plane
	 */
	public void loadClipPlane(Vector4f clipPlane) {
		super.loadVector4f(location_plane, clipPlane);
	}
	
	/**
	 * Loads density to shader
	 * 
	 * @param density	density of fog
	 */
	public void loadDensity(float density) {
		super.loadFloat(location_density, density);
	}
	
	/**
	 * Loads gradient to shader
	 * 
	 * @param gradient	gradient of fog
	 */
	public void loadGradient(float gradient) {
		super.loadFloat(location_gradient, gradient);
	}
	
	/**
	 * Loads maximum light count to shader
	 * 
	 * @param light_count	Light count
	 */
	public void loadMaxLights(int light_count) {
		super.loadInt(location_maxLights, light_count);
	}
	
	/**
	 * Loads tiling factor for terrain texture
	 * 
	 * @param tileFactor		tiling factor for repetition of textures
	 */
	public void loadTileFactor(int tileFactor) {
		super.loadInt(location_tileFactor, tileFactor);
	}
	
}