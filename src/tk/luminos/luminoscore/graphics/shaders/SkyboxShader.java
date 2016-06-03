package tk.luminos.luminoscore.graphics.shaders;

import static tk.luminos.luminoscore.ConfigData.POSITION;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.display.GLFWWindow;
import tk.luminos.luminoscore.graphics.gameobjects.Camera;
import tk.luminos.luminoscore.tools.Maths;

/**
 * 
 * Skybox Shader for Skybox Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class SkyboxShader extends ShaderProgram {
         
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColour;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;
    private int location_lowerLimit;
    private int location_upperLimit;
    
    private float rotation = 0.05f;
	
	public static String VERT = "skybox.vert";
	public static String FRAG = "skybox.frag";
     
    /** 
     * Constructor
     */
    public SkyboxShader() {
        super(VERT, FRAG);
    }
    
    /**
     * Loads projection matrix to shader
     * 
     * @param matrix	Projection Matrix
     */
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    /**
     * Loads view matrix to shader
     * 
     * @param camera	Camera to create view matrix of
     * @param window	{@link GLFWWindow} to get the frame time of
     */
    public void loadViewMatrix(Camera camera, GLFWWindow window){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += window.getFrameTime();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    /**
     * Loads fog to shader
     * 
     * @param r	R value of fog
     * @param g	G value of fog
     * @param b	B value of fog
     */
    public void loadFogColour(float r, float g, float b){
    	super.loadVector(location_fogColour, new Vector3f(r, g, b));
    }
    
    /**
     * Connects texture units
     */
    public void connectTextureUnits(){
    	super.loadInt(location_cubeMap, 0);
    	super.loadInt(location_cubeMap2, 1);
    }
    
    /**
     * Loads blend factor to shader
     * 
     * @param blend	Blend Factor
     */
    public void loadBlendFactor(float blend){
    	super.loadFloat(location_blendFactor, blend);
    }
    
    /**
     * Loads lower rendering limit
     * 
     * @param lowerLimit	Lower rendering limit
     */
    public void loadLowerLimit(float lowerLimit) {
    	super.loadFloat(location_lowerLimit, lowerLimit);
    }
    
    /**
     * Loads upper rendering limit
     * 
     * @param upperLimit	Upper rendering limit
     */
    public void loadUpperLimit(float upperLimit) {
    	super.loadFloat(location_upperLimit, upperLimit);
    }
     
    /*
     * (non-Javadoc)
     * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
     */
    public void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColour = super.getUniformLocation("fogColour");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_lowerLimit = super.getUniformLocation("lowerLimit");
        location_upperLimit = super.getUniformLocation("upperLimit");
    }
 
    /*
     * (non-Javadoc)
     * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
        super.bindAttribute(POSITION, "position");
    }
 
}