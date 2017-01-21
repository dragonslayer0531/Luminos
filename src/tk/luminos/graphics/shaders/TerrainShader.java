package tk.luminos.graphics.shaders;

import static tk.luminos.ConfigData.NORMALS;
import static tk.luminos.ConfigData.POSITION;
import static tk.luminos.ConfigData.TEXTURES;

import java.util.List;

import tk.luminos.graphics.gameobjects.PointLight;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Terrain Shader for Terrain Renderer
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class TerrainShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 4;
	
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
		super.bindAttribute(POSITION, "position");
		super.bindAttribute(TEXTURES, "textureCoordinates");
		super.bindAttribute(NORMALS, "normal");
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
		createUniformPointLights("pointLights");
		createUniformDirectionalLight("sun");
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
	public void loadPointLights(List<PointLight> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				setUniform(getLocation("lightPosition[" + i + "]"), lights.get(i).getPosition());
				setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
				setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
			}else{
				setUniform(getLocation("lightPosition[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("lightColor[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}
	
}