package tk.luminos.graphics.shaders;

/**
 * 
 * Contrast shader to use in Post Processing pipeline
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class ContrastShader extends ShaderProgram implements PostProcess {
	
	public static String VERT = "contrast.vert";
	public static String FRAG = "contrast.frag";
	
	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public ContrastShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	public void getAllUniformLocations() throws Exception {
		createUniform("contrast");
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	public void bindAttributes() {

	}

}
