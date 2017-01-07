package com.luminos.graphics.shaders;

import java.util.List;

import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.maths.vector.Vector4f;

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
	 * @throws Exception 
	 */
	public NormalMapShader() throws Exception {
		super(VERT, FRAG);
	}

	/*
	 * (non-Javadoc)
	 * @see com.luminos.graphics.shaders.ShaderProgram#getAllUniformLocations()
	 */
	public void getAllUniformLocations() {
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
	 * @see com.luminos.graphics.shaders.ShaderProgram#bindAttributes()
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

	/**
	 * Loads lights to shaders
	 * 
	 * @param lights	List of lights
	 */
	public void loadPointLights(List<PointLight> lights, Matrix4f viewMatrix){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				setUniform(getLocation("lightPositionEyeSpace[" + i + "]"), getEyeSpacePosition(lights.get(i), viewMatrix));
				setUniform(getLocation("lightColor[" + i + "]"), lights.get(i).getColor());
				setUniform(getLocation("attenuation[" + i + "]"), lights.get(i).getAttenuation());
			}else{
				setUniform(getLocation("lightPositionEyeSpace[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("lightColor[" + i + "]"), new Vector3f(0, 0, 0));
				setUniform(getLocation("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}

	private Vector3f getEyeSpacePosition(PointLight light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
	}

}
