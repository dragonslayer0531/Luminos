package com.luminos.graphics.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

import com.luminos.filesystem.ResourceLoader;
import com.luminos.graphics.gameobjects.DirectionalLight;
import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.graphics.gameobjects.SpotLight;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.maths.vector.Vector4f;

/**
 * 
 * Base shader program
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public abstract class ShaderProgram {

	private final int programID;
	
	public static final Integer MAX_POINT_LIGHTS = 4;
	public static final Integer MAX_SPOT_LIGHTS = 4;
	
	public static String VERSION = GLSLVersion.getHeader(GLSLVersion.VERSION400, true) + "//\n";
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	public final Map<String, Integer> UNIFORMS = new HashMap<String, Integer>();
	public static final Map<String, String> DEFINES = new HashMap<String, String>();

	/**
	 * Constructor
	 * 
	 * @param vertexFile	Vertex shader file
	 * @param fragmentFile	Fragment shader file
	 * @throws Exception  
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) throws Exception {
		programID = createProgram(loadShader(vertexFile, GL_VERTEX_SHADER), loadShader(fragmentFile, GL_FRAGMENT_SHADER));
		this.start();
		getAllUniformLocations();
		this.stop();
	}

	public void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programID, uniformName);
		if (uniformLocation == -1) {
			System.err.println("COULD NOT LOCATE: " + uniformName + ".  Uniform may not exist or is not used in the "
					+ "final output.");
		}
		UNIFORMS.put(uniformName, uniformLocation);
	}
	
	public void createUniformPointLights(String uniformName) {
		for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
			createUniformPointLight(uniformName, i);
		}
	}
	
	public void createUniformPointLight(String uniformName, int arrayLocation) {
		String name = uniformName + "[" + arrayLocation + "].color";
		int uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
		name = uniformName + "[" + arrayLocation + "].position";
		uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
		name = uniformName + "[" + arrayLocation + "].attenuation";
		uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
	}
	
	public void createUniformPointLight(String uniformName) {
		String name = uniformName + ".color";
		int uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
		name = uniformName + ".position";
		uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
		name = uniformName + ".attenuation";
		uniformLocation = glGetUniformLocation(programID, name);
		UNIFORMS.put(name, uniformLocation);
	}
	
	public void createUniformDirectionalLight(String uniformName) {
		UNIFORMS.put(uniformName + ".color", glGetUniformLocation(programID, uniformName + ".color"));
		UNIFORMS.put(uniformName + ".direction", glGetUniformLocation(programID, uniformName + ".direction"));
		UNIFORMS.put(uniformName + ".intensity", glGetUniformLocation(programID, uniformName + ".intensity"));
	}
	
	public void createUniformSpotLights(String uniformName) {
		for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
			createUniformSpotLight(uniformName, i);
		}
	}
	
	public void createUniformSpotLight(String uniformName, int arrayLocation) {
		createUniformPointLight(uniformName + "[" + arrayLocation + "].light");
		UNIFORMS.put(uniformName + "[" + arrayLocation + "].direction", glGetUniformLocation(programID, uniformName + "[" + arrayLocation + "].direction"));
		UNIFORMS.put(uniformName + "[" + arrayLocation + "].angle", glGetUniformLocation(programID, uniformName + "[" + arrayLocation + "].angle"));
	}
	
	public void createUniformSpotLight(String uniformName) {
		createUniformPointLight(uniformName + ".light");
		UNIFORMS.put(uniformName + ".direction", glGetUniformLocation(programID, uniformName + ".direction"));
		UNIFORMS.put(uniformName + ".angle", glGetUniformLocation(programID, uniformName + ".angle"));
	}

	/**
	 * Gets uniform locations
	 */
	public abstract void getAllUniformLocations();

	/**
	 * Starts shader
	 */
	public void start(){
		glUseProgram(programID);
	}

	/**
	 * Stops shader
	 */
	public void stop(){
		glUseProgram(0);
	}

	/**
	 * Cleans shader up
	 */
	public void cleanUp(){
		stop();
		glDeleteProgram(programID);
	}

	/**
	 * Bind attribute locations
	 */
	public abstract void bindAttributes();

	/**
	 * Binds variable location
	 * 
	 * @param attribute		Attribute to be bound
	 * @param variableName	Variable name to be bound
	 */
	public void bindAttribute(int attribute, String variableName){
		glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void setUniform(Uniform uniform) throws Exception {
		if (uniform.getType().equals(Float.class)) {
			Float f = (float) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Integer.class)) {
			Integer f = (Integer) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Vector2f.class)) {
			Vector2f f = (Vector2f) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Vector3f.class)) {
			Vector3f f = (Vector3f) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Vector4f.class)) {
			Vector4f f = (Vector4f) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Matrix4f.class)) {
			Matrix4f f = (Matrix4f) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else if (uniform.getType().equals(Boolean.class)) {
			Boolean f = (Boolean) uniform.getValue();
			setUniform(uniform.getLocation(), f);
		}
		else {
			throw new Exception("Data Type Not Accepted");
		}
	}
	
	/**
	 * Loads float to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void setUniform(int location, float value) {
		glUniform1f(location, value);
	}
	
	public void setUniform(String name, float value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads integer to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void setUniform(int location, int value){
		glUniform1i(location, value);
	}
	
	public void setUniform(String name, int value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector3f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void setUniform(int location, Vector3f vector){
		glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	public void setUniform(String name, Vector3f value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void setUniform(int location, Vector4f vector){
		glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	public void setUniform(String name, Vector4f value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector2D to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void setUniform(int location, Vector2f vector){
		glUniform2f(location,vector.x,vector.y);
	}
	
	public void setUniform(String name, Vector2f value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Boolean to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void setUniform(int location, boolean value){
		int toLoad = 0;
		if(value){
			toLoad = 1;
		}
		glUniform1i(location, toLoad);
	}
	
	public void setUniform(String name, boolean value) {
		setUniform(getLocation(name), value);
	}
	
	public void setUniformPointLights(String name, List<PointLight> lights) {
		for (int i = 0; i < 4; i++) {
			if (i < lights.size()) {
				setUniform(getLocation(name + "[" + i + "].position"), lights.get(i).getPosition());
				setUniform(getLocation(name + "[" + i + "].color"), lights.get(i).getColor());
				setUniform(getLocation(name + "[" + i + "].attenuation"), lights.get(i).getAttenuation());
			}
			else {
				setUniform(getLocation(name + "[" + i + "].color"), new Vector3f(0, 0, 0));
				setUniform(getLocation(name + "[" + i + "].position"), new Vector3f(0, 0, 0));
				setUniform(getLocation(name + "[" + i + "].attenuation"), new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void setUniformPointLight(String name, PointLight light) {
		setUniform(getLocation(name + ".position"), light.getPosition());
		setUniform(getLocation(name + ".color"), light.getColor());
		setUniform(getLocation(name + "].attenuation"), light.getAttenuation());
	}
	
	public void setUniformDirectionalLight(String name, DirectionalLight light) {
		setUniform(getLocation(name + ".color"), light.getColor());
		setUniform(getLocation(name + ".direction"), light.getDirection());
		setUniform(getLocation(name + ".intensity"), light.getIntensity());
	}
	
	public void setUniformSpotLights(String name, List<SpotLight> lights) {
		for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
			
		}
	}
	
	public void setUniform(String name, Matrix4f value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Matrix4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param matrix	Value of variable to be bound
	 */
	public void setUniform(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(location, false, matrixBuffer);
	}

	public int getLocation(String name) throws NullPointerException {
		return UNIFORMS.get(name);
	}

	//*******************************Private Methods**********************************//

	private String loadDefines() {
		StringBuilder defines = new StringBuilder();
		
		for (String definition : DEFINES.keySet()) {
			defines.append("#define " + definition + " " + DEFINES.get(definition) + "//\n");
		}
		
		return defines.toString();
	}
	
	private int loadShader(String file, int type) throws Exception {
		StringBuilder shaderSource = new StringBuilder();
		shaderSource.append(VERSION);
		shaderSource.append(loadDefines());
		shaderSource.append(ResourceLoader.loadText("/scene.header"));
		shaderSource.append(ResourceLoader.loadText("/" + file));
		String shader = shaderSource.toString();
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shader);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
			throw new Exception(glGetShaderInfoLog(shaderID, 1024));
		}
		return shaderID;
	}
	
	private int createProgram(int vert, int frag) throws Exception {
		int programID = glCreateProgram();
		glAttachShader(programID, vert);
		glAttachShader(programID, frag);
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
			throw new Exception(glGetProgramInfoLog(programID, 1024));
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE)
			throw new Exception(glGetProgramInfoLog(programID, 1024));
		glDetachShader(programID, vert);
		glDetachShader(programID, frag);
		glDeleteShader(vert);
		glDeleteShader(frag);
		return programID;
	}

}
