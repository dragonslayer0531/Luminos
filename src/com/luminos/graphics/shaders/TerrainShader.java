package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.NORMALS;
import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

import java.util.List;

import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.maths.vector.Vector4f;

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
	
	public static String VERT = "terrain.vert";
	public static String FRAG = "terrain.frag";

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public TerrainShader() throws Exception {
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
		createUniform("skyColor");
		createUniform("backgroundTexture");
		createUniform("rTexture");
		createUniform("gTexture");
		createUniform("bTexture");
		createUniform("blendMap");
		createUniform("shadowMap");
//		createUniform("plane");
		createUniform("toShadowMapSpace");
		for(int i = 0; i < MAX_LIGHTS; i++){
			createUniform("lightPosition[" + i + "]");
			createUniform("lightColor[" + i + "]");
			createUniform("attenuation[" + i + "]");
		}
		createUniform("density");
		createUniform("gradient");
		createUniform("maxLights");
		createUniform("tileFactor");
	}
	
	/**
	 * Connect texture units
	 */
	public void connectTextureUnits(){
		setUniform(getLocation("backgroundTexture"), 0);
		setUniform(getLocation("rTexture"), 1);
		setUniform(getLocation("gTexture"), 2);
		setUniform(getLocation("bTexture"), 3);
		setUniform(getLocation("blendMap"), 4);
		setUniform(getLocation("shadowMap"), 5);
	}
	
	/**
	 * Loads shadow space matrix to shader
	 * 
	 * @param matrix	Shadow Space Matrix
	 */
	public void loadToShadowSpaceMatrix(Matrix4f matrix) {
		setUniform(getLocation("toShadowMapSpace"), matrix);
	}
	
	/**
	 * Loads sky color
	 * 
	 * @param skyColor		Sky color
	 */
	public void loadSkyColor(Vector3f skyColor){
		setUniform(getLocation("skyColor"), skyColor);
	}
	
	/**
	 * Loads shine values to shader
	 * 
	 * @param damper		Damper value
	 * @param reflectivity	Reflectivity value
	 */
	public void loadShineVariables(float damper, float reflectivity){
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
	 * Loads lights to shader
	 * 
	 * @param lights	List of {@link PointLight}s
	 */
	public void loadPointLights(List<PointLight> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				setUniform(getLocation("lightPosition[" + i + "]"), lights.get(i).getPosition());
				setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
				setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
			}else{
				setUniform(getLocation("lightPosition[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("lightColor[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}
	
	/**
	 * Loads view matrix to shader
	 * 
	 * @param camera	Camera to load view matrix of
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
	 * @param clipPlane	Clipping plane
	 */
	public void loadClipPlane(Vector4f clipPlane) {
//		setUniform(getLocation("plane"), clipPlane);
	}
	
	/**
	 * Loads density to shader
	 * 
	 * @param density	density of fog
	 */
	public void loadDensity(float density) {
		setUniform(getLocation("density"), density);
	}
	
	/**
	 * Loads gradient to shader
	 * 
	 * @param gradient	gradient of fog
	 */
	public void loadGradient(float gradient) {
		setUniform(getLocation("gradient"), gradient);
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
	 * Loads tiling factor for terrain texture
	 * 
	 * @param tileFactor		tiling factor for repetition of textures
	 */
	public void loadTileFactor(int tileFactor) {
		setUniform(getLocation("tileFactor"), tileFactor);
	}
	
}