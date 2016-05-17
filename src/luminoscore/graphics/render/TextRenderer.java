package luminoscore.graphics.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.shaders.TextShader;
import luminoscore.graphics.text.FontType;
import luminoscore.graphics.text.GUIText;
import luminoscore.graphics.text.TextMeshData;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Renders text to screen
 *
 */

public class TextRenderer {
	
	private Loader loader;
	
	private TextShader shader;
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	
	/**
	 * @param loader	Defines loader to render with
	 * 
	 * Constructor
	 */
	public TextRenderer(Loader loader) {
		this.shader = new TextShader();
		this.loader = loader;
	}
	
	/**
	 * Renders Text to screen
	 */
	public void render() {
		prepare();
		for(FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		stop();
	}
	
	/**
	 * Cleans up shader
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
	
	/**
	 * @param text	GUIText to load to GPU
	 * 
	 * Loads text to GPU
	 */
	public void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);	
		}
		textBatch.add(text);
	}
	
	/**
	 * @param text	GUIText to remove from GPU
	 * 
	 * Removes text from GPU
	 */
	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

//*****************************************Private Methods*********************************************//
	
	/**
	 * Prepares for rendering
	 */
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
	}
	
	/**
	 * @param text	GUIText to be rendered
	 * 
	 * Renders individual GUIText
	 */
	private void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Stops the renderer
	 */
	private void stop() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}

}
