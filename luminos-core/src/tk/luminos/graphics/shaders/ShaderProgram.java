package tk.luminos.graphics.shaders;

import org.lwjgl.BufferUtils;
import tk.luminos.Engine;
import tk.luminos.filesystem.ResourceLoader;
import tk.luminos.graphics.DirectionalLight;
import tk.luminos.graphics.PointLight;
import tk.luminos.graphics.SpotLight;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * 
 * Base shader program
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public abstract class ShaderProgram {

	public int programID;
	
	public static final Integer SCENE_POINT_LIGHTS = 4;
	public static final Integer SCENE_SPOT_LIGHTS = 4;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	protected final Map<String, Integer> UNIFORMS = new HashMap<String, Integer>();
	protected static final Map<String, String> DEFINES = new HashMap<String, String>();
	
	private String vertex, fragment;
	
	public ShaderProgram(String vertex, String fragment) {
		this.vertex = vertex;
		this.fragment = fragment;
		
		init();
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 * @throws Exception		Thrown if the uniform location is -1, which means the uniform does
	 * 							not exist of is compile out of the code.  If it is compiled out
	 * 							of the code, it had no effect (direct or indirect) on the final
	 * 							outputs of the respective shader.
	 */
	public final void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programID, uniformName);
		if (uniformLocation < 0) {
			throw new RuntimeException("Could not locate uniform variable: " + uniformName + ".  Uniform may not exist or is not used in the "
					+ "final output.");
		}
		UNIFORMS.put(uniformName, uniformLocation);
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for an array of point lights.  By default, 
	 * it will create 4 in the shader. 
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 */
	public final void createUniformPointLights(String uniformName) {
		for (int i = 0; i < SCENE_POINT_LIGHTS; i++) {
			createUniformPointLight(uniformName, i);
		}
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for a single point light in an array.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 * @param arrayLocation		Location in array of point lights
	 */
	public final void createUniformPointLight(String uniformName, int arrayLocation) {
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
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for a single point light.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 */
	public final void createUniformPointLight(String uniformName) {
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
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for a single directional light.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 */
	public final void createUniformDirectionalLight(String uniformName) {
		UNIFORMS.put(uniformName + ".color", glGetUniformLocation(programID, uniformName + ".color"));
		UNIFORMS.put(uniformName + ".direction", glGetUniformLocation(programID, uniformName + ".direction"));
		UNIFORMS.put(uniformName + ".intensity", glGetUniformLocation(programID, uniformName + ".intensity"));
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for an array of spot lights.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 */
	public final void createUniformSpotLights(String uniformName) {
		for (int i = 0; i < SCENE_SPOT_LIGHTS; i++) {
			createUniformSpotLight(uniformName, i);
		}
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for a single spot light in an array.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 * @param arrayLocation		Location in array of spot lights
	 */
	public final void createUniformSpotLight(String uniformName, int arrayLocation) {
		createUniformPointLight(uniformName + "[" + arrayLocation + "].light");
		UNIFORMS.put(uniformName + "[" + arrayLocation + "].direction", glGetUniformLocation(programID, uniformName + "[" + arrayLocation + "].direction"));
		UNIFORMS.put(uniformName + "[" + arrayLocation + "].angle", glGetUniformLocation(programID, uniformName + "[" + arrayLocation + "].angle"));
	}
	
	/**
	 * Gets the uniform in the current shader and attaches it to the relevant
	 * uniform location map.  This caches the uniform, as glGetUniformLocation() 
	 * is a slow method.  This is used for a single point light.
	 * 
	 * @param uniformName		Name of uniform to get location of from GPU
	 */
	public final void createUniformSpotLight(String uniformName) {
		createUniformPointLight(uniformName + ".light");
		UNIFORMS.put(uniformName + ".direction", glGetUniformLocation(programID, uniformName + ".direction"));
		UNIFORMS.put(uniformName + ".angle", glGetUniformLocation(programID, uniformName + ".angle"));
	}

	/**
	 * Gets uniform locations
	 * @throws Exception	Thrown if shader cannot find location of uniform
	 */
	public abstract void getAllUniformLocations() throws Exception;

	/**
	 * Starts shader
	 */
	public final void start() {
		glUseProgram(programID);
	}

	/**
	 * Stops shader
	 */
	public final void stop() {
		glUseProgram(0);
	}

	/**
	 * Cleans shader up
	 */
	public final void dispose() {
		stop();
		glDeleteProgram(programID);
	}

	/**
	 * Bind attribute locations
	 * 
	 * Only needs to be used when attribute locations are not specified in
	 * the shader.  The location qualifier is core in versions 330 and higher,
	 * and should be used when possible.
	 */
	public abstract void bindAttributes();

	/**
	 * Binds variable location
	 * 
	 * @param attribute		Attribute to be bound
	 * @param variableName	Variable name to be bound
	 */
	public final void bindAttribute(int attribute, String variableName){
		glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Loads float to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(int location, float value) {
		glUniform1f(location, value);
	}
	
	/**
	 * Loads float to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(String name, float value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads integer to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(int location, int value){
		glUniform1i(location, value);
	}
	
	/**
	 * Loads integer to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(String name, int value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector3f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public final void setUniform(int location, Vector3 vector){
		glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	/**
	 * Loads Vector3f to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(String name, Vector3 value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public final void setUniform(int location, Vector4 vector){
		glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	/**
	 * Loads Vector4f to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value	Value of variable to be bound
	 */
	public final void setUniform(String name, Vector4 value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Vector2D to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param vector	Value of variable to be bound
	 */
	public final void setUniform(int location, Vector2 vector){
		glUniform2f(location,vector.x,vector.y);
	}
	
	/**
	 * Loads Vector2D to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(String name, Vector2 value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Boolean to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(int location, boolean value){
		glUniform1i(location, value ? 1 : 0);
	}
	
	/**
	 * Loads Boolean to shader
	 * 
	 * @param name		Name of variable to be bound
	 * @param value		Value of variable to be bound
	 */
	public final void setUniform(String name, boolean value) {
		setUniform(getLocation(name), value);
	}
	
	/**
	 * Loads a list of point lights to shader
	 * 
	 * @param name		Name of uniform (No []) to be bound
	 * @param lights	Value of variable to be bound
	 */
	public final void setUniformPointLights(String name, List<PointLight> lights) {
		for (int i = 0; i < 4; i++) {
			if (i < lights.size()) {
				setUniform(getLocation(name + "[" + i + "].position"), lights.get(i).getPosition());
				setUniform(getLocation(name + "[" + i + "].color"), lights.get(i).getColor());
				setUniform(getLocation(name + "[" + i + "].attenuation"), lights.get(i).getAttenuation());
			}
			else {
				setUniform(getLocation(name + "[" + i + "].color"), new Vector3(0, 0, 0));
				setUniform(getLocation(name + "[" + i + "].position"), new Vector3(0, 0, 0));
				setUniform(getLocation(name + "[" + i + "].attenuation"), new Vector3(1, 0, 0));
			}
		}
	}
	
	/**
	 * Loads a point light to shader
	 * 
	 * @param name			Name of uniform to be bound
	 * @param light			Value of variable to be bound
	 */
	public final void setUniformPointLight(String name, PointLight light) {
		setUniform(getLocation(name + ".position"), light.getPosition());
		setUniform(getLocation(name + ".color"), light.getColor());
		setUniform(getLocation(name + ".attenuation"), light.getAttenuation());
	}
	
	/**
	 * Loads a directional light to shader
	 * 
	 * @param name			Name of uniform to be bound
	 * @param light			Value of variable to be bound
	 */
	public final void setUniformDirectionalLight(String name, DirectionalLight light) {
		setUniform(getLocation(name + ".color"), light.getColor());
		setUniform(getLocation(name + ".direction"), light.getDirection());
		setUniform(getLocation(name + ".intensity"), light.getIntensity());
	}
	
	/**
	 * Loads a list of spot lights to shader
	 * 
	 * @param name			Name of uniform (No []) to be bound
	 * @param lights		Value of variable to be bound
	 */
	public final void setUniformSpotLights(String name, List<SpotLight> lights) {
		for (int i = 0; i < SCENE_SPOT_LIGHTS; i++) {
			
		}
	}
	
	/**
	 * Loads Matrix4f to shader
	 * 
	 * @param name			Name of variable to be bound
	 * @param value			Value of variable to be bound
	 */
	public final void setUniform(String name, Matrix4 value) {
		setUniform(getLocation(name), value);
	}

	/**
	 * Loads Matrix4f to shader
	 * 
	 * @param location	Location of variable to be bound
	 * @param matrix	Value of variable to be bound
	 */
	public final void setUniform(int location, Matrix4 matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(location, false, matrixBuffer);
	}

	/**
	 * Gets the location of a cached uniform
	 * 
	 * @param name		Name of variable to search for
	 * @return			Location of uniform variable in current shader
	 */
	public final int getLocation(String name) {
		try {
			return UNIFORMS.get(name);
		}
		catch (NullPointerException e) {
			int loc = glGetUniformLocation(programID, name);
			if (loc > -1) {
				UNIFORMS.put(name, loc);
				return loc;
			}
			throw new RuntimeException("Could not locate uniform variable: " + name + "\nLocation given: " + loc);
		}
	}

	//*******************************Private Methods**********************************//

	
	private void init() {
		programID = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertexShader, genShader(vertex));
		glShaderSource(fragmentShader, genShader(fragment));
		
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException(vertex + "\n" + glGetShaderInfoLog(vertexShader, 1024));
		}
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException(fragment + "\n" + glGetShaderInfoLog(fragmentShader, 1024));
		}
		
		glAttachShader(programID, vertexShader);
		glAttachShader(programID, fragmentShader);
		
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetProgramInfoLog(programID, 1024));
		}
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetShaderInfoLog(programID, 1024));
		}
		
		glDetachShader(programID, vertexShader);
		glDetachShader(programID, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}
	
	private String genShader(String file) {
		StringBuilder shaderSource = new StringBuilder();
		shaderSource.append(GLSLVersion.getHeader(Engine.GLSL_VERSION, true) + "//\n");
		shaderSource.append(loadDefines());
		try {
			shaderSource.append(ResourceLoader.loadText("/scene.header"));
			shaderSource.append(ResourceLoader.loadText("/functions.header"));
			shaderSource.append(ResourceLoader.loadText("/" + file));
		}
		catch (Exception e) {
			throw new RuntimeException("Could not create shader: " + file + "\n" + "File could not be loaded!");
		}
		return shaderSource.toString();
	}
	
	private String loadDefines() {
		StringBuilder defines = new StringBuilder();
		
		for (String definition : DEFINES.keySet()) {
			defines.append("#define " + definition + " " + DEFINES.get(definition) + "//\n");
		}
		
		return defines.toString();
	}

}
