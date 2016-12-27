package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;

import com.luminos.maths.matrix.Matrix4f;

/**
 * 
 * Shadow Shader for Shadow Renderers
 *
 * @author Nick Clark
 * @version 1.1
 *
 */

public class ShadowShader extends ShaderProgram {

    private int location_mvpMatrix;
    
    public static String VERT = "shadow.vert";
    public static String FRAG = "shadow.frag";
    
	public ShadowShader() {
        super(VERT, FRAG);
    }
 
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
    public void getAllUniformLocations() {
        location_mvpMatrix = super.getUniformLocation("mvpMatrix");  
    }
     
    /**
     * Loads Model View Projection matrix to shader
     * 
     * @param mvpMatrix		Model View Projection matrix	
     */
    public void loadMvpMatrix(Matrix4f mvpMatrix){
        super.loadMatrix4f(location_mvpMatrix, mvpMatrix);
    }
 
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
        super.bindAttribute(POSITION, "in_position");
        super.bindAttribute(1,  "in_tesxtureCoords");
    }
	
}
