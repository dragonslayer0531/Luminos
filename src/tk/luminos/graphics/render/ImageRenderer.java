package tk.luminos.graphics.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.ConfigData;
import tk.luminos.graphics.loaders.Loader;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.ImageShader;

public class ImageRenderer {
	
	private static final float[] POSITIONS = {
			-1, 1, -1, -1, 1, 1, 1, -1
	};
	
	private RawModel quad;
	private ImageShader shader;

	public ImageRenderer(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		shader = new ImageShader();
	}
	
	public void render(int textureID) {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(ConfigData.POSITION);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		shader.start();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.stop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(ConfigData.POSITION);
		GL30.glBindVertexArray(0);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
