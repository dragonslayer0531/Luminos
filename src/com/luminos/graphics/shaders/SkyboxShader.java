package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.ConfigData;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.tools.Maths;

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
    private int location_fogColor;
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
     */
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += 1f / ConfigData.FPS * 0.001f;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    /**
     * Loads fog to shader
     * 
     * @param color		Fog color
     */
    public void loadFogColor(Vector3f color){
    	super.loadVector3f(location_fogColor, color);
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
     * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
     */
    public void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_lowerLimit = super.getUniformLocation("lowerLimit");
        location_upperLimit = super.getUniformLocation("upperLimit");
    }
 
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
        super.bindAttribute(POSITION, "position");
    }
 
}