package tk.luminos.luminoscore.graphics.shaders;

import static tk.luminos.luminoscore.ConfigData.POSITION;

/**
 * 
 * Creates Image Shader for Image Renderer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ImageShader extends ShaderProgram {

	public static String vertexFile = "image.vert";
	public static String fragmentFile = "image.frag";
	
	/**
	 * Constructor
	 */
	public ImageShader() {
		super(vertexFile, fragmentFile);
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}

}
