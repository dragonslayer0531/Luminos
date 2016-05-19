package tk.luminos.luminoscore.graphics.shaders;

import tk.luminos.luminoscore.GlobalLock;

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
	protected void getAllUniformLocations() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
	}

}
