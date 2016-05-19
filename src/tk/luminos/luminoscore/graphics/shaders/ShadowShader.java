package tk.luminos.luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

import tk.luminos.luminoscore.GlobalLock;

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
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
    protected void getAllUniformLocations() {
        location_mvpMatrix = super.getUniformLocation("mvpMatrix");  
    }
     
    /**
     * Loads Model View Projection matrix to shader
     * 
     * @param mvpMatrix		Model View Projection matrix	
     */
    public void loadMvpMatrix(Matrix4f mvpMatrix){
        super.loadMatrix(location_mvpMatrix, mvpMatrix);
    }
 
    /*
     * (non-Javadoc)
     * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
     */
    protected void bindAttributes() {
        super.bindAttribute(GlobalLock.POSITION, "in_position");
    }
	
}
