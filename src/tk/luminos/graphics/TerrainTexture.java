package tk.luminos.graphics;

/**
 * 
 * Texture used for terrains
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TerrainTexture implements Texture {
	
	private int textureID;

	/**
	 * Constructor
	 * 
	 * @param textureID	GPU ID of Texture
	 */
	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}

	/**
	 * Gets Texture ID
	 * 
	 * @return Texture ID
	 */
	public int getID() {
		return textureID;
	}
	
}
