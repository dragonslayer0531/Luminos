package tk.luminos.graphics.opengl.shaders.postprocess;

/**
 * 
 * Interface to be used by all shaders in the post processing pipeline
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public interface PostProcess {
	
	/**
	 * Starts the shader.  This method should be the same as in the ShaderProgram
	 */
	public void start();
	
	/**
	 * Stops the shader.  This method should be the same as in the ShaderProgram
	 */
	public void stop();
	
	/**
	 * Cleans up the shader.  This method should be the same as in the ShaderProgram
	 */
	public void cleanUp();

}
