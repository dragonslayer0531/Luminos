package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.ConfigData;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;
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
    
    private float rotation = 0.05f;
	
	public static String VERT = "skybox.vert";
	public static String FRAG = "skybox.frag";
     
    /** 
     * Constructor
     * @throws Exception 
     */
    public SkyboxShader() throws Exception {
        super(VERT, FRAG);
    }
    
    /**
     * Loads projection matrix to shader
     * 
     * @param matrix	Projection Matrix
     */
    public void loadProjectionMatrix(Matrix4f matrix){
        setUniform(getLocation("projectionMatrix"), matrix);
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
        setUniform(getLocation("viewMatrix"), matrix);
    }
    
    /**
     * Loads fog to shader
     * 
     * @param color		Fog color
     */
    public void loadFogColor(Vector3f color){
    	setUniform(getLocation("fogColor"), color);
    }
    
    /**
     * Connects texture units
     */
    public void connectTextureUnits(){
    	setUniform(getLocation("cubeMap"), 0);
    	setUniform(getLocation("cubeMap2"), 1);
    }
    
    /**
     * Loads blend factor to shader
     * 
     * @param blend	Blend Factor
     */
    public void loadBlendFactor(float blend){
    	setUniform(getLocation("blendFactor"), blend);
    }
    
    /**
     * Loads lower rendering limit
     * 
     * @param lowerLimit	Lower rendering limit
     */
    public void loadLowerLimit(float lowerLimit) {
    	setUniform(getLocation("lowerLimit"), lowerLimit);
    }
    
    /**
     * Loads upper rendering limit
     * 
     * @param upperLimit	Upper rendering limit
     */
    public void loadUpperLimit(float upperLimit) {
    	setUniform(getLocation("upperLimit"), upperLimit);
    }
     
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
     */
    public void getAllUniformLocations() {
        createUniform("projectionMatrix");
        createUniform("viewMatrix");
        createUniform("fogColor");
        createUniform("blendFactor");
        createUniform("cubeMap");
        createUniform("cubeMap2");
        createUniform("lowerLimit");
        createUniform("upperLimit");
    }
 
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
        super.bindAttribute(POSITION, "position");
    }
 
}