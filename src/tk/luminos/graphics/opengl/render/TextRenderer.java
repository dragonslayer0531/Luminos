package tk.luminos.graphics.opengl.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.opengl.loaders.Loader;
import tk.luminos.graphics.opengl.shaders.TextShader;
import tk.luminos.graphics.opengl.text.FontType;
import tk.luminos.graphics.opengl.text.GUIText;
import tk.luminos.graphics.opengl.text.TextMeshData;

/**
 * 
 * Renders text to screen
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TextRenderer {
	
	private Loader loader;
	
	private TextShader shader;
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	
	/**
	 * Constructor
	 * 
	 * @param shader	Defines shader to render with
	 * @param loader	Defines loader to render with
	 */
	public TextRenderer(TextShader shader, Loader loader) {
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
	 * Loads text to GPU
	 * 
	 * @param text	GUIText to load to GPU
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
	 * Removes text from GPU
	 * 
	 * @param text	GUIText to remove from GPU
	 */
	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	/**
	 * Updates the text value
	 * 
	 * @param text		Text to update
	 * @param updated	String of updated value
	 */
	public void updateText(GUIText text, String updated){
		GUIText update = new GUIText(updated, text.getFontSize(), text.getFont(), text.getPosition(), text.getMaxLineSize(), text.centerText);
		removeText(text);
		loadText(update);
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
	 * Renders individual GUIText
	 * 
	 * @param text	GUIText to be rendered
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
