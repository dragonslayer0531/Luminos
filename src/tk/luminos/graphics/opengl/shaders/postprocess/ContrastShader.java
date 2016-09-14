package tk.luminos.graphics.opengl.shaders.postprocess;

import tk.luminos.graphics.opengl.shaders.ShaderProgram;

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
	
	private int location_contrast;

	/**
	 * Constructor
	 */
	public ContrastShader() {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	public void getAllUniformLocations() {
		location_contrast = super.getUniformLocation("contrast");
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	public void bindAttributes() {

	}
	
	/**
	 * Sets contrast factor.  Default should be set to 0
	 * 
	 * @param contrast		Contrast factor
	 */
	public void setContrast(float contrast) {
		super.loadFloat(location_contrast, contrast);
	}

}
