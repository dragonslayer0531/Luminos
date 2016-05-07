package luminoscore.graphics.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.GlobalLock;
import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.entities.Light;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * Entity Shader to use in Entity Renderer
 *
 */
public class EntityShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	
	public static String VERT = "entity.vert";
	public static String FRAG = "entity.frag";

	public EntityShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
		super.bindAttribute(GlobalLock.TEXTURES, "textureCoordinates");
		super.bindAttribute(GlobalLock.NORMALS, "normal");
	}
	
	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	/**
	 * @param numberOfRows	Number of rows in texture
	 * 
	 * Loads rows of texture to shader
	 */
	public void loadNumberOfRows(int numberOfRows){
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	/**
	 * @param x	X offset
	 * @param y	Y offset
	 * 
	 * Loads offset to shader
	 */
	public void loadOffset(float x, float y){
		super.load2DVector(location_offset, new Vector2f(x,y));
	}
	
	/**
	 * @param r	R value of sky
	 * @param g	G value of sky
	 * @param b B value of sky
	 * 
	 * Loads sky color
	 */
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	/**
	 * @param useFake	Fake lighting
	 * 
	 * Loads boolean for whether to use fake lighting or not
	 */
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	/**
	 * @param damper		Damper factor of texture
	 * @param reflectivity	Reflective factor of texture
	 * 
	 * Loads shine variables to shader
	 */
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**
	 * @param matrix	Transformation matrix
	 * 
	 * Loads transformation matrix to shader
	 */
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * @param lights	List of lights
	 * 
	 * Loads lights to shaders
	 */
	public void loadLights(List<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	/**
	 * @param camera	Camera to make view matrix
	 * 
	 * Loads view matrix to shader
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * @param projection	Projection matrix
	 * 
	 * Loads projection matrix to shader
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * @param plane	Clip plane
	 * 
	 * Loads clip plane to shader
	 */
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(location_plane, plane);
	}

}
