package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.NORMALS;
import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import java.util.List;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.Light;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;
import com.luminos.tools.Maths;

/**
 * 
 * Entity Shader to use in Entity Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class GameObjectShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 20;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_density;
	private int location_gradient;
	private int location_maxLights;
	private int location_toShadowMapSpace;
	
	public static String VERT = "entity.vert";
	public static String FRAG = "entity.frag";

	/**
	 * Constructor
	 */
	public GameObjectShader() {
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
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColor = super.getUniformLocation("skyColor");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_density = super.getUniformLocation("density");
		location_gradient = super.getUniformLocation("gradient");
		location_maxLights = super.getUniformLocation("maxLights");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
	}
	
	/**
	 * Load density to shader
	 * 
	 * @param density		Density of fog
	 */
	public void loadDensity(float density) {
		super.loadFloat(location_density, density);
	}
	
	/**
	 * Load gradient to shader
	 * 
	 * @param gradient		Gradient of fog
	 */
	public void loadGradient(float gradient) {
		super.loadFloat(location_gradient, gradient);
	}
	
	/**
	 * Loads rows of texture to shader
	 * 
	 * @param numberOfRows	Number of rows in texture
	 */
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	/**
	 * Loads offset to shader
	 * 
	 * @param x	X offset
	 * @param y	Y offset
	 */
	public void loadOffset(float x, float y){
		super.loadVector2f(location_offset, new Vector2f(x,y));
	}
	
	/**
	 * Loads sky color
	 * 
	 * @param r	R value of sky
	 * @param g	G value of sky
	 * @param b B value of sky
	 */
	public void loadSkyColor(float r, float g, float b){
		super.loadVector3f(location_skyColor, new Vector3f(r,g,b));
	}
	
	/**
	 * Loads boolean for whether to use fake lighting or not
	 * 
	 * @param useFake	Fake lighting
	 */
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	/**
	 * Loads shine variables to shader
	 * 
	 * @param damper		Damper factor of texture
	 * @param reflectivity	Reflective factor of texture
	 */
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**
	 * Loads transformation matrix to shader
	 * 
	 * @param matrix	Transformation matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Loads lights to shaders
	 * 
	 * @param lights	List of lights
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
	 * @param camera	Camera to make view matrix
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Loads clip plane to shader
	 * 
	 * @param plane	Clip plane
	 */
	public void loadClipPlane(Vector4f plane) {
		super.loadVector4f(location_plane, plane);
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
	 * Loads shadow map space to shader
	 * 
	 * @param shadowMapSpace		Shadow map space
	 */
	public void loadToShadowMapSpace(Matrix4f shadowMapSpace) {
		super.loadMatrix(location_toShadowMapSpace, shadowMapSpace);
	}

}
