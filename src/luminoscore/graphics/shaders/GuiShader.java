package luminoscore.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

import luminoscore.GlobalLock;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 *
 * Gui Shader to use in GuiRenderer
 */

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "res/shaders/gui.vert";
	private static final String FRAGMENT_FILE = "res/shaders/gui.frag";
	
	private int location_transformationMatrix;

	/**
	 * Constructor
	 */
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	/*
	 * (non-Javadoc)
	 * @see luminoscore.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(GlobalLock.POSITION, "position");
	}
	
	/**
	 * @param matrix	Transformation matrix
	 * 
	 * Load transformation matrix to shader
	 */
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}


}
