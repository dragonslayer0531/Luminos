package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.PostProcess;
import tk.luminos.loaders.Loader;

/**
 * 
 * Post processing render pipeline
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class PostProcessRenderer {
	
	private static final float[] POSITIONS = {
			-1, 1, -1, -1, 1, 1, 1, -1
	};
	private List<PostProcess> processes = new ArrayList<PostProcess>();
	private RawModel quad;
	
	/**
	 * Constructor
	 * 
	 * @param loader		Loads quad to GPU
	 */
	public PostProcessRenderer(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
	}
	
	/**
	 * Loads shader to pipeline
	 * 
	 * @param process		{@link PostProcess} to add to pipeline
	 */
	public void loadShader(PostProcess process) {
		processes.add(process);
	}
	
	/**
	 * Removes shader from pipeline
	 * 
	 * @param process		{@link PostProcess} to remove from pipeline
	 */
	public void removeShader(PostProcess process) {
		process.dispose();
		processes.remove(process);
	}
	
	/**
	 * Processes the texture holding the scene
	 * 
	 * @param textureID		Texture to be processed
	 */
	public void render(int textureID) {
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glDisable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE0);
		for(PostProcess shader : processes) {
			shader.start();
			glBindTexture(GL_TEXTURE_2D, textureID);
			glClear(GL_COLOR_BUFFER_BIT);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			shader.stop();
		}
		glEnable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	/**
	 * Cleans up all post processing shaders
	 */
	public void dispose() {
		for(PostProcess shader : processes) shader.dispose();
	}
	
	/**
	 * Removes all shaders from the post processing pipeline
	 */
	public void clearAllShaders() {
		for(PostProcess process : processes) {
			removeShader(process);
		}
	}

}
