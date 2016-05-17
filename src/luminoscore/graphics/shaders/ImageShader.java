package luminoscore.graphics.shaders;

import luminoscore.GlobalLock;

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
