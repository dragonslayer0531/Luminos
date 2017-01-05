package com.luminos.graphics.render;

/**
 * Renders texture to screen
 * 
 * @author Nick Clark
 * @version 1.0
 */
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

import com.luminos.ConfigData;
import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.shaders.ImageShader;
import com.luminos.loaders.Loader;

public class ImageRenderer {
	
	private static final float[] POSITIONS = {
			-1, 1, -1, -1, 1, 1, 1, -1
	};
	
	private RawModel quad;
	private ImageShader shader;

	/**
	 * Constructor
	 * 
	 * @param loader		Creates quad to render to
	 * @throws Exception 
	 */
	public ImageRenderer(Loader loader) throws Exception {
		quad = loader.loadToVAO(POSITIONS, 2);
		shader = new ImageShader();
	}
	
	/**
	 * Renders texture to quad
	 * 
	 * @param textureID		texture to be rendered to quad
	 */
	public void render(int textureID) {
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(ConfigData.POSITION);
		glDisable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE0);
		shader.start();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glClear(GL_COLOR_BUFFER_BIT);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		shader.stop();
		glEnable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(ConfigData.POSITION);
		glBindVertexArray(0);
	}
	
	/**
	 * Cleans up {@link ImageShader} program
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

}
