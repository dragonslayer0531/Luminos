package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.POSITION;
import static tk.luminos.ConfigData.TEXTURES;


/**
 * 
 * Text Shader for Text Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class TextShader extends ShaderProgram {
	
	public static String VERT = "text.vert";
	public static String FRAG = "text.frag";
	
	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public TextShader() throws Exception {
		super(VERT, FRAG);
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
		createUniform("color");
		createUniform("translation");
		createUniform("font");
	}
	
	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(POSITION, "position");
		super.bindAttribute(TEXTURES, "textureCoords");
	}

}
