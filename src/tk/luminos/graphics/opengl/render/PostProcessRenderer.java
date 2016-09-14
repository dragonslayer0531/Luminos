package tk.luminos.graphics.opengl.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.ConfigData;
import tk.luminos.graphics.opengl.loaders.Loader;
import tk.luminos.graphics.opengl.models.RawModel;
import tk.luminos.graphics.opengl.shaders.postprocess.PostProcess;


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
		process.cleanUp();
		processes.remove(process);
	}
	
	/**
	 * Processes the texture holding the scene
	 * 
	 * @param textureID		Texture to be processed
	 */
	public void render(int textureID) {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(ConfigData.POSITION);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		for(PostProcess shader : processes) {
			shader.start();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			shader.stop();
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(ConfigData.POSITION);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Cleans up all post processing shaders
	 */
	public void cleanUp() {
		for(PostProcess shader : processes) shader.cleanUp();
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
