package tk.luminos.graphics.opengl.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.opengl.loaders.Loader;
import tk.luminos.graphics.opengl.models.RawModel;
import tk.luminos.graphics.opengl.shaders.GuiShader;
import tk.luminos.graphics.opengl.textures.GUITexture;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.tools.Maths;

/**
 * 
 * Allows for the rendering of 2D GUIs
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;
	
	/**
	 * Constructor of GuiRenderer
	 * 
	 * @param shader	Defines shader to render with
	 * @param loader 	Loader to use in rendering
	 */
	public GuiRenderer(GuiShader shader, Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		this.shader = shader;
	}
	
	/**
	 * Renders GUITextures to screen
	 * 
	 * @param guis		List of {@link GUITexture}s to be rendered
	 */
	public void render(List<GUITexture> guis){
		
		if(!guis.isEmpty()) {
			shader.start();
			GL30.glBindVertexArray(quad.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			for(GUITexture gui: guis){
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getID());
				Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
				shader.loadTransformation(matrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
			shader.stop();
		}
		
	}
	
	/**
	 * Cleans up shader
	 */
	public void cleanUp(){
		shader.cleanUp();
	}
}
