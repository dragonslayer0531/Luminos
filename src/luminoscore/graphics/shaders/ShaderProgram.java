package luminoscore.graphics.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import luminoscore.Debug;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * Base shader program
 *
 */
public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * @param vertexFile	Vertex shader file
	 * @param fragmentFile	Fragment shader file
	 * 
	 * Constructor file
	 */
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	/**
	 * Gets uniform locations
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * @param uniformName	Shader uniform variable
	 * @return int			Shader variable location
	 * 
	 * Gets uniform location of variable
	 */
	protected int getUniformLocation(String uniformName){
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
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Bind attribute locations
	 */
	protected abstract void bindAttributes();
	
	/**
	 * @param attribute		Attribute to be bound
	 * @param variableName	Variable name to be bound
	 * 
	 * Binds variable location
	 */
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 * 
	 * Loads float to shader
	 */
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 * 
	 * Loads integer to shader
	 */
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 * 
	 * Loads Vector3f to shader
	 */
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 * 
	 * Loads Vector4f to shader
	 */
	protected void load4DVector(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 * 
	 * Loads Vector2D to shader
	 */
	protected void load2DVector(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 * 
	 * Loads Boolean to shader
	 */
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * @param location	Location of variable to be bound
	 * @param matrix	Value of variable to be bound
	 * 
	 * Loads Matrix4f to shader
	 */
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
//*******************************Private Methods**********************************//
	
	/**
	 * @param file	Shader file to load
	 * @param type	Type of shader to load
	 * @return int	ID of shader
	 * 
	 * Loads shader to GPU
	 */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch(IOException e) {
			Debug.addData(ShaderProgram.class + " " + e.getMessage());
			Debug.addData(e.getMessage());
			Debug.print();
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			Debug.addData(ShaderProgram.class + " " + GL20.glGetShaderInfoLog(shaderID, 50000) + " Could not compile shader.");
			Debug.print();
		}
		return shaderID;
	}

}
