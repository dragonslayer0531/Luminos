package com.luminos.graphics.shaders;

import static com.luminos.ConfigData.NORMALS;
import static com.luminos.ConfigData.POSITION;
import static com.luminos.ConfigData.TEXTURES;

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
	 * @throws Exception 
	 */
	public GameObjectShader() throws Exception {
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
	public void getAllUniformLocations() {
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
		createUniformDirectionalLight("sun");
		
//		for (String s : super.uniforms.keySet()) {
//			System.out.println(s + ", " + super.uniforms.get(s));
//		}
	}
	
	public void connectTextureUnits() {

	}

}
