package luminoscore.graphics.shaders;

import java.util.List;

import luminoscore.graphics.entities.Light;
import luminoscore.graphics.entities.components.Camera;
import luminoscore.util.math.Matrix4f;
import luminoscore.util.math.MatrixCreator;
import luminoscore.util.math.Vector2f;
import luminoscore.util.math.Vector3f;
import luminoscore.util.math.Vector4f;

public class EntityShader extends ShaderProgram {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Universal Entity Shader Data
	private static final int MAX_LIGHTS = ShaderProgram.MAX_LIGHTS;
	
	//Uniform Locations
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
	
	/*
	 * @param vertex Defines vertex shader location
	 * @param fragment Defines fragment shader location
	 * 
	 * Constructor using super class
	 */
	public EntityShader(String vertex, String fragment) {
		super(vertex, fragment);
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
	}
	
	/*
	 * @param numberOfRows
	 * 
	 * Loads the number of rows to the shader
	 */
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	/*
	 * @param x X Offset
	 * @param y Y Offset
	 * 
	 * Loads texture coordinate offsets
	 */
	public void loadOffset(float x, float y){
		super.load2DVector(location_offset, new Vector2f(x,y));
	}
	
	/*
	 * @param r Red value
	 * @param g Green Value
	 * @param b Blue Value
	 * 
	 * Loads sky color for blending
	 */
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(location_skyColor, new Vector3f(r,g,b));
	}
	
	/*
	 * @param useFake Fake lighting
	 * 
	 * Determines whether to use "fake" lighting on the entity
	 */
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	/*
	 * @param damper Damper of the shine
	 * @param reflectivity Reflectiveness of the entity
	 * 
	 * Loads the shine to the shader
	 */
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/*
	 * @param matrix Defines Transformation Matrix
	 * 
	 * Loads transformation matrix to the shader
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix4f(location_transformationMatrix, matrix);
	}
	
	/*
	 * @param lights List of lights
	 * 
	 * Loads lights to use in rendering to the shader
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
	 * @param camera
	 * 
	 * Defines the camera to use in the creation of the shader's view matrix
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
		super.loadMatrix4f(location_viewMatrix, viewMatrix);
	}
	
	/*
	 * @param projection Defines projection matrix
	 * 
	 * Loads projection matrix to shader
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix4f(location_projectionMatrix, projection);
	}
	
	/*
	 * @param plane
	 * 
	 * Loads clip plane to shader
	 */
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(location_plane, plane);
	}

}
