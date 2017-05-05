package tk.luminos.graphics.shaders;

import java.util.List;

import tk.luminos.graphics.PointLight;

/**
 * 
 * Terrain Shader for Terrain Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class TerrainShader extends ShaderProgram{

	public static String VERT = "terrain.vert";
	public static String FRAG = "terrain.frag";

	/**
	 * Constructor
	 * @throws Exception 		Thrown if shader file cannot be found, compiled, validated
	 * 							or linked
	 */
	public TerrainShader() throws Exception {
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
		createUniform("skyColor");
		createUniform("backgroundTexture");
		createUniform("rTexture");
		createUniform("gTexture");
		createUniform("bTexture");
		createUniform("blendMap");
		createUniform("shadowMap");
		createUniform("toShadowMapSpace");
		createUniform("density");
		createUniform("gradient");
		createUniform("tileFactor");
		createUniform("numPointLights");
		createUniformPointLights("pointLights");
		createUniformDirectionalLight("sun");
		createUniform("pcfCount");
		createUniform("shadowDistance");
		createUniform("transitionDistance");
		createUniform("useWater");
	}

	/**
	 * Connect texture units
	 */
	public void connectTextureUnits(){
		setUniform(getLocation("backgroundTexture"), 0);
		setUniform(getLocation("rTexture"), 1);
		setUniform(getLocation("gTexture"), 2);
		setUniform(getLocation("bTexture"), 3);
		setUniform(getLocation("blendMap"), 4);
		setUniform(getLocation("shadowMap"), 5);
	}

	/**
	 * Loads lights to shader
	 * 
	 * @param lights	List of {@link PointLight}s
	 */
	public void loadPointLights(List<PointLight> lights) {
		for (int i = 0; i < lights.size(); i++) {
			if (i >= 20)
				break;
			setUniform(getLocation("lightPosition[" + i + "]"), lights.get(i).getPosition());
			setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
			setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
		}
	}

}