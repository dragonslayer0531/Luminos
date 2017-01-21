package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.POSITION;

import tk.luminos.graphics.shaders.postprocess.PostProcess;

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
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public ImageShader() throws Exception {
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
