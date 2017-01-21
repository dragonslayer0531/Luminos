package tk.luminos.graphics.shaders;

/**
 * 
 * Normal Map Shader for Normal Map Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class NormalMapShader extends ShaderProgram {

	public static String VERT = "normal.vert";
	public static String FRAG = "normal.frag";

	private static final int MAX_LIGHTS = 4;

	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public NormalMapShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
		createUniform("transformationMatrix");
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("skyColor");
		createUniform("numberOfRows");
		createUniform("offset");
		createUniform("plane");
		createUniform("modelTexture");
//		createUniform("normalMap");
		for (int i=0;i<MAX_LIGHTS;i++) {
			createUniform("lightPositionEyeSpace[" + i + "]");
			createUniform("lightColor[" + i + "]");
			createUniform("attenuation[" + i + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see tk.luminos.graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	/**
	 * Connects textures to normal map
	 */
	public void connectTextureUnits() {
		setUniform(getLocation("modelTexture"), 0);
//		setUniform(getLocation("normalMap"), 1);
	}

}
