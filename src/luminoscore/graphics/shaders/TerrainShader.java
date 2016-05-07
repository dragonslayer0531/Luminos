package luminoscore.graphics.shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
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
 * Terrain Shader for Terrain Renderer
 *
 */

public class TerrainShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 4;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_shineDamper;
	private int location_attenuation[];
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_shadowMap;
	private int location_plane;
	private int location_toShadowMapSpace;
	
	public static String VERT = "terrain.vert";
	public static String FRAG = "terrain.frag";

	/**
	 * @param vert	Vertex shader file
	 * @param frag	Fragment shader file
	 */
	public TerrainShader() {
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
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_shadowMap = super.getUniformLocation("shadowMap");
		location_plane = super.getUniformLocation("plane");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		
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
	 * @param matrix	Shadow Space Matrix
	 * 
	 * Loads shadow space matrix to shader
	 */
	public void loadToShadowSpaceMatrix(Matrix4f matrix) {
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}
	
	/**
	 * @param r	R color of sky
	 * @param g	G color of sky
	 * @param b	B color of sky
	 * 
	 * Loads sky color
	 */
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	/**
	 * @param damper		Damper value
	 * @param reflectivity	Reflectivity value
	 * 
	 * Loads shine values to shader
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
	 * Loads lights to shader
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
	 * @param camera	Camera to load view matrix of
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
	 * @param clipPlane	Clipping plane
	 * 
	 * Loads clip plane to shader
	 */
	public void loadClipPlane(Vector4f clipPlane) {
		super.load4DVector(location_plane, clipPlane);
	}
	
}