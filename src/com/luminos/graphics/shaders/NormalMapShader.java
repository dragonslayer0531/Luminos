package com.luminos.graphics.shaders;

import java.util.List;

import com.luminos.graphics.gameobjects.Light;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;

/**
 * 
 * Normal Map Shader for Normal Map Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class NormalMapShader extends ShaderProgram {

	public static String VERT = "normal.vert";
	public static String FRAG = "normal.frag";

	private static final int MAX_LIGHTS = 20;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPositionEyeSpace[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_modelTexture;
	private int location_normalMap;
	private int location_maxLights;

	/**
	 * Constructor
	 */
	public NormalMapShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_normalMap = super.getUniformLocation("normalMap");
		location_lightPositionEyeSpace = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_maxLights = super.getUniformLocation("maxLights");

	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	/**
	 * Connects textures to normal map
	 */
	public void connectTextureUnits(){
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_normalMap, 1);
	}

	/**
	 * Loads clip plane to the shader
	 * 
	 * @param plane		Clip plane of rendering
	 */
	public void loadClipPlane(Vector4f plane){
		super.loadVector4f(location_plane, plane);
	}

	/**
	 * Sets number of rows involved in the texture
	 * 
	 * @param numberOfRows	rows of texture
	 */
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}

	/**
	 * Sets the texture offset
	 * 
	 * @param x		X offset
	 * @param y		Y offset
	 */
	public void loadOffset(float x, float y){
		super.loadVector2f(location_offset, new Vector2f(x,y));
	}

	/**
	 * Loads sky color
	 * 
	 * @param r		Red component
	 * @param g		Green component
	 * @param b		Blue component
	 */
	public void loadSkyColour(float r, float g, float b){
		super.loadVector3f(location_skyColor, new Vector3f(r,g,b));
	}

	/**
	 * Loads shine and reflection
	 * 
	 * @param damper			Shine damper
	 * @param reflectivity		Reflection factor
	 */
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	/**
	 * Loads transformation matrix to shader
	 * 
	 * @param matrix		Transformation matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/**
	 * Loads lights to shader
	 * 
	 * @param lights		Lights
	 * @param viewMatrix	View matrix of light
	 */
	public void loadLights(List<Light> lights, Matrix4f viewMatrix){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector3f(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
				super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector3f(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	/**
	 * Loads view matrix to shader
	 * 
	 * @param viewMatrix		View matrix
	 */
	public void loadViewMatrix(Matrix4f viewMatrix){
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection		Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Loads maximum number of lights to be processed by the shader
	 * 
	 * @param maxLights		Maximum number of lights
	 */
	public void loadMaxLights(int maxLights) {
		super.loadInt(location_maxLights, maxLights);
	}

	private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix){
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
	}

}
