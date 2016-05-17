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
	
	public static String VERT = "gui.vert";
	public static String FRAG = "gui.frag";
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VERT, FRAG);
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
