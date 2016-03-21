package luminoscore.graphics.text;

import java.io.File;

import luminoscore.graphics.display.GLFWwindow;

public class FontType {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */

	//Constructor Fields
	private int textureAtlas;
	private TextMeshCreator loader;
	
	/*
	 * @param textureAtlas Defines GPU ID for the font
	 * @param fontFile Defines the font to be loaded
	 * @param display Used for aspect ratio
	 * 
	 * Constructor
	 */
	public FontType(int textureAtlas, File fontFile, GLFWwindow display) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile, display);
	}

	//Getter Methods
	public int getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshData loadText(GUIText text) {
		return loader.createTextMesh(text);
	}

}
