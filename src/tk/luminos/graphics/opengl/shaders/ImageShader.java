package tk.luminos.graphics.opengl.shaders;

import static tk.luminos.ConfigData.POSITION;

import tk.luminos.graphics.opengl.shaders.postprocess.PostProcess;

/**
 * 
 * Creates Image Shader for Image Renderer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ImageShader extends ShaderProgram implements PostProcess {

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
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}

}
