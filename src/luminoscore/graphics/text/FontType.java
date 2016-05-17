package luminoscore.graphics.text;

import java.io.File;

/**
 * 
 * Font Type Wrapper
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class FontType {

	private int textureAtlas;
    private TextMeshCreator loader;

    /**
     * Constructor
     * 
     * @param textureAtlas	Texture Atlas of Font
     * @param fontFile		Font file name
     */
    public FontType(int textureAtlas, File fontFile) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile);
    }
 
    /**
     * Gets the GPU ID of the texture atlas
     * 
     * @return Texture atlas ID
     */
    public int getTextureAtlas() {
        return textureAtlas;
    }
 
    /**
     * Creates text mesh data
     * 
     * @param text	GUI Text to render
     * @return		text mesh data of font
     */
    public TextMeshData loadText(GUIText text) {
        return loader.createTextMesh(text);
    }
	
}
