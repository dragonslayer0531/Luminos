package luminoscore.graphics.text;

import java.io.File;

import luminoscore.graphics.display.GLFWWindow;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Font Type Wrapper
 *
 */

public class FontType {

	private int textureAtlas;
    private TextMeshCreator loader;

    /**
     * @param textureAtlas	Texture Atlas of Font
     * @param fontFile		Font file name
     * @param window		Window to render to
     * 
     * Constructor
     */
    public FontType(int textureAtlas, File fontFile, GLFWWindow window) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile, window);
    }
 
    /**
     * @return int	Texture atlas ID
     * 
     * Gets the GPU ID of the texture atlas
     */
    public int getTextureAtlas() {
        return textureAtlas;
    }
 
    /**
     * @param text	GUI Text to render
     * @return		Creates text mesh data
     */
    public TextMeshData loadText(GUIText text) {
        return loader.createTextMesh(text);
    }
	
}
