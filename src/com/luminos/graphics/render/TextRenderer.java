package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luminos.graphics.shaders.TextShader;
import com.luminos.graphics.text.FontType;
import com.luminos.graphics.text.GUIText;
import com.luminos.graphics.text.TextMeshData;
import com.luminos.loaders.Loader;

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
		this.shader = shader;
		this.loader = loader;
	}
	
	/**
	 * Renders Text to screen
	 */
	public void render() {
		prepare();
		for(FontType font : texts.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
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
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
	}
	
	/**
	 * Renders individual GUIText
	 * 
	 * @param text	GUIText to be rendered
	 */
	private void renderText(GUIText text) {
		glBindVertexArray(text.getMesh());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
	
	/**
	 * Stops the renderer
	 */
	private void stop() {
		shader.stop();
		glDisable(GL_BLEND);
	}

}
