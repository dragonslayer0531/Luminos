package com.luminos.graphics.shaders;

import java.util.List;

import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.maths.vector.Vector4f;

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

	private static final int MAX_LIGHTS = 4;

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public NormalMapShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		createUniform("transformationMatrix");
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("skyColor");
		createUniform("numberOfRows");
		createUniform("offset");
		createUniform("plane");
		createUniform("modelTexture");
//		createUniform("normalMap");
		for (int i=0;i<MAX_LIGHTS;i++) {
			createUniform("lightPositionEyeSpace[" + i + "]");
			createUniform("lightColor[" + i + "]");
			createUniform("attenuation[" + i + "]");
		}
		createUniform("maxLights");

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
	public void connectTextureUnits() {
		setUniform(getLocation("modelTexture"), 0);
//		setUniform(getLocation("normalMap"), 1);
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
	 * Loads sky color
	 * 
	 * @param r	R value of sky
	 * @param g	G value of sky
	 * @param b B value of sky
	 */
	public void loadSkyColor(float r, float g, float b){
		setUniform(getLocation("skyColor"), new Vector3f(r,g,b));
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
	public void loadPointLights(List<PointLight> lights, Matrix4f viewMatrix){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				setUniform(getLocation("lightPositionEyeSpace[" + i + "]"), getEyeSpacePosition(lights.get(i), viewMatrix));
				setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
				setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
			}else{
				setUniform(getLocation("lightPositionEyeSpace[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("lightColor[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}

	/**
	 * Loads view matrix to shader
	 * 
	 * @param camera	Camera to make view matrix
	 */
	public void loadViewMatrix(Matrix4f viewMatrix){
		setUniform(getLocation("viewMatrix"), viewMatrix);
	}

	/**
	 * Loads projection matrix to shader
	 * 
	 * @param projection	Projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		setUniform(getLocation("projectionMatrix"), projection);
	}

	/**
	 * Loads clip plane to shader
	 * 
	 * @param plane	Clip plane
	 */
	public void loadClipPlane(Vector4f plane) {
		setUniform(getLocation("plane"), plane);
	}

	/**
	 * Loads maximum light count to shader
	 * 
	 * @param light_count	PointLight count
	 */
	public void loadMaxPointLights(int light_count) {
		setUniform(getLocation("maxLights"), light_count);
	}

	/**
	 * Loads shadow map space to shader
	 * 
	 * @param shadowMapSpace		Shadow map space
	 */
	public void loadToShadowMapSpace(Matrix4f shadowMapSpace) {
		setUniform(getLocation("toShadowMapSpace"), shadowMapSpace);
	}

	private Vector3f getEyeSpacePosition(PointLight light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
	}

}
