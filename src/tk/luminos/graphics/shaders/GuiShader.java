package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.POSITION;

/**
 * 
 * Gui Shader to use in GuiRenderer
 *
 * @author Nick Clark
 * @version 1.1
 */

public class GuiShader extends ShaderProgram {
	
	public static String VERT = "gui.vert";
	public static String FRAG = "gui.frag";

	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public GuiShader() throws Exception {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
		createUniform("transformationMatrix");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
	}

}
