package luminoscore.graphics.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import luminoscore.util.math.matrix.Matrix4f;
import luminoscore.util.math.vector.Vector2f;
import luminoscore.util.math.vector.Vector3f;
import luminoscore.util.math.vector.Vector4f;

public abstract class ShaderProgram {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Data fields
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	protected static int MAX_LIGHTS = 4;
	//FloatBuffer
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/*
	 * @param vertexFile String containing the location of the vertex file
	 * @param fragmentFile String containing the location of the fragment file
	 * 
	 * Constructor
	 */
	protected ShaderProgram(Shader shader) {
		vertexShaderID = loadShader(shader.getVertexShader(), GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(shader.getFragmentShader(), GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	/*
	 * @param file Defines file name
	 * @param type Defines shader type
	 * @return int
	 * 
	 * Processes shader and returns its ID
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSourceCode = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSourceCode.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSourceCode);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}
	
	//Abstract method binding attributes to a location
	protected abstract void bindAttributes();
	
	/*
	 * @param attribute Defines the attribute to be bound
	 * @param variableName Defines the variable to be bound
	 * 
	 * Binds an variable to and attribute
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	//Gets the uniform variable locations
	protected abstract void getAllUniformLocations();
	
	/*
	 * @param uniformName Defines the uniform variable to find
	 * @return int
	 * 
	 * Returns attribute location of uniform
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/*
	 * @param location Defines the location
	 * @param value Defines the value
	 * 
	 * Loads a float to an attribute
	 */
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/*
	 * @param location Defines the location
	 * @param value Defines the value
	 * 
	 * Loads an integer to an attribute
	 */
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	/*
	 * @param location Defines the location
	 * @param vector Defines the vector
	 * 
	 * Loads a 2D Vector to an attribute
	 */
	protected void load2DVector(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	
	/*
	 * @param location Defines the location
	 * @param vector Defines the vector
	 * 
	 * Loads a 3D Vector to an attribute
	 */
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	/*
	 * @param location Defines the location
	 * @param vector Defines the vector
	 * 
	 * Loads a 4D vector to an attribute
	 */
	protected void load4DVector(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	/*
	 * @param location Defines the location
	 * @param value Defines the value
	 * 
	 * Loads a boolean to an attribute
	 * 0 is false
	 * 1 is true
	 */
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/*
	 * @param location Defines the location
	 * @param matrix Defines the matrix
	 * 
	 * Loads a 4x4 Matrix to an attribute via a FloatBuffer object
	 */
	protected void loadMatrix4f(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	//Starts the program
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	//Ends the program
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	//Detaches and deletes the shaders and the program
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

}
