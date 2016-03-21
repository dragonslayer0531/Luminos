package luminoscore.graphics.shaders;

import java.util.List;

import luminoscore.graphics.entities.Light;
import luminoscore.graphics.entities.components.Camera;
import luminoscore.util.math.matrix.Matrix4f;
import luminoscore.util.math.matrix.MatrixCreator;
import luminoscore.util.math.vector.Vector3f;
import luminoscore.util.math.vector.Vector4f;

public class TerrainShader extends ShaderProgram {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Universe Terrain Shader Data
	private static final int MAX_LIGHTS = ShaderProgram.MAX_LIGHTS;

	//Uniform Locations
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
	private int location_plane;
	
	/*
	 * @param shader Defines the shader
	 * 
	 * Constructor using super class
	 */
	public TerrainShader(Shader shader) {
		super(shader);
	}

	//Binds the location of the position, texture coordinates, and normals
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	//Sets all uniform locations
	protected void getAllUniformLocations() {
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
		location_plane = super.getUniformLocation("plane");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}		
	}
	
	//Connects texture units for blendmap
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	/*
	 * @param r Red component
	 * @param g Green component
	 * @param b Blue component
	 * 
	 * Loads sky color to shader
	 */
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(location_skyColor, new Vector3f(r,g,b));
	}
	
	/*
	 * @param damper Damper of shine
	 * @param reflectivity Reflectivity of shine
	 * 
	 * Loads shine variables to the shader
	 */
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/*
	 * @param matrix Transformation matrix
	 * 
	 * Loads transformation matrix to the shader
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix4f(location_transformationMatrix, matrix);
	}
	
	/*
	 * @param lights List of lights
	 * 
	 * Loads light data to the shader
	 */
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	/*
	 * @param camera Defines camera
	 * 
	 * Loads view matrix to the shader
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
		super.loadMatrix4f(location_viewMatrix, viewMatrix);
	}
	
	/*
	 * @param projection Projection matrix
	 * 
	 * Loads projection matrix to the shader
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix4f(location_projectionMatrix, projection);
	}
	
	/*
	 * @param clipPlane
	 * 
	 * Loads clipPlane to the shader
	 */
	public void loadClipPlane(Vector4f clipPlane) {
		super.load4DVector(location_plane, clipPlane);
	}

}
