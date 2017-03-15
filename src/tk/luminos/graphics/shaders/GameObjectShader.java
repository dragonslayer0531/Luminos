package tk.luminos.graphics.shaders;

/**
 * 
 * Entity Shader to use in Entity Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class GameObjectShader extends ShaderProgram {
	
	public static String VERT = "gameObject.vert";
	public static String FRAG = "gameObject.frag";

	/**
	 * Constructor
	 * @throws Exception		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public GameObjectShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {

	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
		createUniform("transformationMatrix");
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("useFakeLighting");
		createUniform("skyColor");
		createUniform("numberOfRows");
		createUniform("offset");
		createUniformPointLights("pointLights");
		createUniform("density");
		createUniform("gradient");
		createUniform("modelTexture");
		createUniform("numPointLights");
//		createUniform("numSpotLights");
		createUniformDirectionalLight("sun");
	}
	
	/**
	 * Connects the texture units to locations
	 */
	public void connectTextureUnits() {

	}

}
