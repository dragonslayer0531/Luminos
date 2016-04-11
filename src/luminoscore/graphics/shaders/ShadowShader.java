package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

import luminoscore.GlobalLock;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 *
 * Shadow Shader for Shadow Renderers
 *
 */

public class ShadowShader extends ShaderProgram {

    private int location_mvpMatrix;
	
    /**
     * @param VERT	Vertex Shader file
     * @param FRAG	Fragment Shader file
     */
	public ShadowShader(String VERT, String FRAG) {
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
     * @param mvpMatrix
     * 
     * Loads Model View Projection matrix to shader
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
