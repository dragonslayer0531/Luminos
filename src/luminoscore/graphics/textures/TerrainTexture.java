package luminoscore.graphics.textures;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Texture used for terrains
 *
 */

public class TerrainTexture {
	
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
	public int getTextureID() {
		return textureID;
	}
	
}
