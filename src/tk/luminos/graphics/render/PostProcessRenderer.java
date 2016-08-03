package tk.luminos.graphics.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.ConfigData;
import tk.luminos.graphics.loaders.Loader;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.postprocess.PostProcess;

public class PostProcessRenderer {
	
	private static final float[] POSITIONS = {
			-1, 1, -1, -1, 1, 1, 1, -1
	};
	private List<PostProcess> processes = new ArrayList<PostProcess>();
	private RawModel quad;
	
	public PostProcessRenderer(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
	}
	
	public void loadShader(PostProcess process) {
		processes.add(process);
	}
	
	public void removeShader(PostProcess process) {
		process.cleanUp();
		processes.remove(process);
	}
	
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
	
	public void cleanUp() {
		for(PostProcess shader : processes) shader.cleanUp();
	}

}
