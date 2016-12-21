package com.luminos.graphics.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import com.luminos.Debug;
import com.luminos.LuminosException;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector2f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.maths.vector.Vector4f;

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
	
	/**
	 * Constructor
	 * 
	 * @param vertexFile	Vertex shader file
	 * @param fragmentFile	Fragment shader file
	 */
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		getAllUniformLocations();
	}
	
	public ShaderProgram(String vertexFile, String geometryFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, geometryShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(geometryShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		getAllUniformLocations();
	}
	
	/**
	 * Gets uniform locations
	 */
	public abstract void getAllUniformLocations();
	
	/**
	 * Gets uniform location of variable
	 * 
	 * @param uniformName	Shader uniform variable
	 * @return 	Shader variable location
	 */
	public int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID,uniformName);
	}
	
	/**
	 * Starts shader
	 */
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Stops shader
	 */
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	/**
	 * Cleans shader up
	 */
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		if (geometryShaderID != -1) 
			GL20.glDetachShader(programID, geometryShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteProgram(programID);
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
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Loads float to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Loads integer to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Loads Vector3f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void loadVector3f(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	/**
	 * Loads Vector4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void loadVector4f(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	/**
	 * Loads Vector2D to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public void loadVector2f(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	
	/**
	 * Loads Boolean to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * Loads Matrix4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param matrix	Value of variable to be bound
	 */
	public void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
//*******************************Private Methods**********************************//
	
	/**
	 * Loads shader to GPU
	 * 
	 * @param file	Shader file to load
	 * @param type	Type of shader to load
	 * @return      ID of shader
	 */
	private static int loadShader(String file, int type){
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
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 50000));
			Debug.addData(new LuminosException(GL20.glGetShaderInfoLog(shaderID, 50000) + ": " + type + " SHADER FAILED TO COMPILE"));
			Debug.print();
		}
		return shaderID;
	}

}
