package tk.luminos.graphics.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL41.*;

import tk.luminos.Engine;
import tk.luminos.filesystem.ResourceLoader;

public class ShaderPipeline {
	
	private String vertex, fragment;
	private int pipeline;
	
	public ShaderPipeline(String vertex, String fragment) {
		this.vertex = vertex;
		this.fragment = fragment;
		
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws Exception {
		int programID = glCreateProgram();
		glProgramParameteri(programID, GL_PROGRAM_SEPARABLE, GL_TRUE);
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertexShader, genShader(vertex));
		glShaderSource(fragmentShader, genShader(fragment));
		
		glCompileShader(vertexShader);
		glCompileShader(fragmentShader);
		
		glAttachShader(programID, vertexShader);
		glAttachShader(programID, fragmentShader);
		
		glLinkProgram(programID);
		
		glDetachShader(programID, vertexShader);
		glDetachShader(programID, fragmentShader);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		pipeline = glGenProgramPipelines();
		glUseProgramStages(pipeline, GL_VERTEX_SHADER_BIT | GL_FRAGMENT_SHADER_BIT, programID);
	}
	
	private String genShader(String file) throws Exception {
		StringBuilder shaderSource = new StringBuilder();
		shaderSource.append(GLSLVersion.getHeader(Engine.GLSL_VERSION, true) + "//\n");
		shaderSource.append(ResourceLoader.loadText("/scene.header"));
		shaderSource.append(ResourceLoader.loadText("/functions.header"));
		shaderSource.append(ResourceLoader.loadText("/" + file));
		return shaderSource.toString();
	}
	


}
