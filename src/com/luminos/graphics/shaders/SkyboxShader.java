package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.ConfigData;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;

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
     * Loads view matrix to shader
     * 
     * @param camera	Camera to create view matrix of
     */
    public Matrix4f createViewMatrix(Matrix4f matrix){
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += 1f / ConfigData.FPS * 0.001f;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        return matrix;
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
    
    public void connectTextureUnits() {
    	super.setUniform("cubeMap", 0);
    	super.setUniform("cubeMap2", 1);
    }
 
}