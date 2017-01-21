package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.POSITION;

import tk.luminos.graphics.gameobjects.PointLight;

/**
 * 
 * Water Shader for Water Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class WaterShader extends ShaderProgram {

	public static String VERT = "water.vert";
	public static String FRAG = "water.frag";

	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public WaterShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#bindAttributes()
	 */
	public void bindAttributes() {
		bindAttribute(POSITION, "position");
	}

	/*
	 * (non-Javadoc)
	 * @see graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() throws Exception {
		createUniform("projectionMatrix");
		createUniform("viewMatrix");
		createUniform("modelMatrix");
		createUniform("reflectionTexture");
		createUniform("refractionTexture");
		createUniform("dudvMap");
		createUniform("normalMap");
		createUniform("moveFactor");
		createUniform("cameraPosition");
		createUniform("lightPosition");
		createUniform("lightColor");
		createUniform("depthMap");
		createUniform("near");
		createUniform("far");
		createUniform("tiling");
		createUniform("waveStrength");
		createUniform("shineDamper");
		createUniform("reflectivity");
		createUniform("skyColor");
	}

	/**
	 * Connect texture units
	 */
	public void connectTextureUnits() {
		setUniform(getLocation("reflectionTexture"), 0);
		setUniform(getLocation("refractionTexture"), 1);
		setUniform(getLocation("dudvMap"), 2);
		setUniform(getLocation("normalMap"), 3);
		setUniform(getLocation("depthMap"), 4);
	}
	
	/**
	 * Loads {@link PointLight} to shader
	 * 
	 * @param sun	Focal light of scene 
	 */
	public void loadPointLight(PointLight sun) {
		setUniform(getLocation("lightColor"), sun.getColor());
		setUniform(getLocation("lightPosition"), sun.getPosition());
	}

}
