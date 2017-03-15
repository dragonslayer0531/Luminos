package tk.luminos.graphics.render;

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

import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.ImageShader;
import tk.luminos.loaders.Loader;

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
	 * @throws Exception 	Thrown if shader file cannot be found, compiled, validated
	 * 						or linked
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
		glEnableVertexAttribArray(0);
		glDisable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE0);
		shader.start();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glClear(GL_COLOR_BUFFER_BIT);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		shader.stop();
		glEnable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	/**
	 * Cleans up {@link ImageShader} program
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

}
