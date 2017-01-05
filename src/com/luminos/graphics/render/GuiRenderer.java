package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.shaders.GuiShader;
import com.luminos.graphics.textures.GUITexture;
import com.luminos.loaders.Loader;
import com.luminos.tools.Maths;
import com.luminos.tools.maths.matrix.Matrix4f;

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
			glBindVertexArray(quad.getVaoID());
			glEnableVertexAttribArray(0);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDisable(GL_DEPTH_TEST);
			for(GUITexture gui: guis){
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, gui.getID());
				Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
				shader.loadTransformation(matrix);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
			glEnable(GL_DEPTH_TEST);
			glDisable(GL_BLEND);
			glDisableVertexAttribArray(0);
			glBindVertexArray(0);
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
