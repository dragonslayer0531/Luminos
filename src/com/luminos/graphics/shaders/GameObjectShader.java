package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.NORMALS;
import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import java.util.List;

import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * Entity Shader to use in Entity Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class GameObjectShader extends ShaderProgram {
	
	

	private static final int MAX_LIGHTS = 4;

	public static String VERT = "entity.vert";
	public static String FRAG = "entity.frag";

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public GameObjectShader() throws Exception {
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
		createUniform("transformationMatrix");
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("useFakeLighting");
		createUniform("skyColor");
		createUniform("numberOfRows");
		createUniform("offset");
		for(int i=0;i<MAX_LIGHTS;i++){
			createUniform("lightPosition[" + i + "]");
			createUniform("lightColor[" + i + "]");
			createUniform("attenuation[" + i + "]");
		}
		createUniform("density");
		createUniform("gradient");
		createUniform("modelTexture");
		
//		for (String s : super.uniforms.keySet()) {
//			System.out.println(s + ", " + super.uniforms.get(s));
//		}
	}
	
	public void connectTextureUnits() {

	}

	/**
	 * Load density to shader
	 * 
	 * @param density		Density of fog
	 */
	public void loadDensity(float density) {
		setUniform(getLocation("density"), density);
	}

	/**
	 * Load gradient to shader
	 * 
	 * @param gradient		Gradient of fog
	 */
	public void loadGradient(float gradient) {
		setUniform(getLocation("gradient"), gradient);
	}

	/**
	 * Loads rows of texture to shader
	 * 
	 * @param numberOfRows	Number of rows in texture
	 */
	public void loadNumberOfRows(int numberOfRows){
		setUniform(getLocation("numberOfRows"), numberOfRows);
	}

	/**
	 * Loads offset to shader
	 * 
	 * @param x	X offset
	 * @param y	Y offset
	 */
	public void loadOffset(float x, float y){
		setUniform(getLocation("offset"), new Vector2f(x,y));
	}
	
	/**
	 * Loads boolean for whether to use fake lighting or not
	 * 
	 * @param useFake	Fake lighting
	 */
	public void loadFakePointLightingVariable(boolean useFake){
		setUniform(getLocation("useFakeLighting"), useFake);
	}

	/**
	 * Loads shine variables to shader
	 * 
	 * @param damper		Damper factor of texture
	 * @param reflectivity	Reflective factor of texture
	 */
	public void loadShineVariables(float damper,float reflectivity){
		setUniform(getLocation("shineDamper"), damper);
		setUniform(getLocation("reflectivity"), reflectivity);
	}

	/**
	 * Loads transformation matrix to shader
	 * 
	 * @param matrix	Transformation matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		setUniform(getLocation("transformationMatrix"), matrix);
	}

	/**
	 * Loads lights to shaders
	 * 
	 * @param lights	List of lights
	 */
	public void loadPointLights(List<PointLight> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			if(i < lights.size()){
				setUniform(getLocation("lightPosition[" + i + "]"), lights.get(i).getPosition());
				setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
				setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
			} 
			else {
				setUniform(getLocation("lightPosition[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("lightColor[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}

	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		setUniform(getLocation("projectionMatrix"), projection);
	}

}
