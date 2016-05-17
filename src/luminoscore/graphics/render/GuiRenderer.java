package luminoscore.graphics.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.shaders.GuiShader;
import luminoscore.graphics.textures.GuiTexture;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Allows for the rendering of 2D GUIs
 *
 */
public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;
	
	/**
	 * @param loader 	Loader to use in rendering
	 * 
	 * Constructor of GuiRenderer
	 */
	public GuiRenderer(Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	/**
	 * @param guis		List GuiTextures to be rendered
	 * 
	 * Renders GuiTextures to screen
	 */
	public void render(List<GuiTexture> guis){
		
		if(!guis.isEmpty()) {
			shader.start();
			GL30.glBindVertexArray(quad.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			for(GuiTexture gui: guis){
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
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
