package tk.luminos.graphics.shaders;

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
    
    /**
     * Constructor
     * @throws Exception		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
     */
	public ShadowShader() throws Exception {
        super(VERT, FRAG);
    }
 
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
    public void getAllUniformLocations() throws Exception {
       createUniform("mvpMatrix");  
    }
 
    /*
     * (non-Javadoc)
     * @see graphics.shaders.ShaderProgram#bindAttributes()
     */
    public void bindAttributes() {
    	
    }
	
}
