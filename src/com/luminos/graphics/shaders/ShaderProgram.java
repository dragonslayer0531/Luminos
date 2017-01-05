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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32;

import com.luminos.Debug;
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

	private int programID;
	private int vertexShaderID;
	private int geometryShaderID = -1;
	private int fragmentShaderID;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	final Map<String, Integer> uniforms = new HashMap<String, Integer>();

	/**
	 * Constructor
	 * 
	 * @param vertexFile	Vertex shader file
	 * @param fragmentFile	Fragment shader file
	 * @throws Exception  
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) throws Exception {
		vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
		}
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
		}
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		getAllUniformLocations();
	}

	public ShaderProgram(String vertexFile, String geometryFile, String fragmentFile) throws Exception {
		vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, geometryShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
		}
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
		}
		glDeleteShader(vertexShaderID);
		glDeleteShader(geometryShaderID);
		glDeleteShader(fragmentShaderID);
		getAllUniformLocations();
	}

	public void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programID, uniformName);
		if (uniformLocation == -1) {
			System.out.println("COULD NOT LOCATE:" + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
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
		glDetachShader(programID, vertexShaderID);
		if (geometryShaderID != -1) 
			glDetachShader(programID, geometryShaderID);
		glDetachShader(programID, fragmentShaderID);
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

	/**
	 * Loads float to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void setUniform(int location, float value){
		glUniform1f(location, value);
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

	/**
	 * Loads Vector3f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void setUniform(int location, Vector3f vector){
		glUniform3f(location,vector.x,vector.y,vector.z);
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

	/**
	 * Loads Vector2D to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void setUniform(int location, Vector2f vector){
		glUniform2f(location,vector.x,vector.y);
	}

	/**
	 * Loads Boolean to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void setUniform(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		glUniform1f(location, toLoad);
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

	public int getLocation(String name) {
		return uniforms.get(name);
	}

	//*******************************Private Methods**********************************//

	/**
	 * Loads shader to GPU
	 * 
	 * @param file	Shader file to load
	 * @param type	Type of shader to load
	 * @return      ID of shader
	 * @throws Exception 
	 */
	private static int loadShader(String file, int type) throws Exception {
		StringBuilder shaderSource = new StringBuilder();

		InputStream isr = Class.class.getResourceAsStream("/scene.header");
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch(IOException e) {
			Debug.addData(e);
			Debug.print();
		}

		isr = Class.class.getResourceAsStream("/" + file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(isr));
		String line;
		while((line = reader.readLine())!=null){
			shaderSource.append(line).append("//\n");
		}
		reader.close();
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS )== GL_FALSE){
			throw new Exception(glGetShaderInfoLog(shaderID, 50000) + ": " + type + " SHADER FAILED TO COMPILE");
		}
		return shaderID;
	}

}
