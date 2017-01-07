package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

/**
 * 
 * Shadow Shader for Shadow Renderers
 *
 * @author Nick Clark
 * @version 1.1
 *
 */

public class ShadowShader extends ShaderProgram {
    
    public static String VERT = "shadow.vert";
    public static String FRAG = "shadow.frag";
    
	public ShadowShader() throws Exception {
        super(VERT, FRAG);
    }
 
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
    public void getAllUniformLocations() {
       createUniform("mvpMatrix");  
    }
 
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
        super.bindAttribute(POSITION, "in_position");
        super.bindAttribute(TEXTURES, "in_textureCoords");
    }
	
}
