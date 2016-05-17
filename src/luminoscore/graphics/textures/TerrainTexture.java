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
	 * @param textureID	GPU ID of Texture
	 * 
	 * Constructor
	 */
	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}

	/**
	 * @return int	Texture ID
	 * 
	 * Gets Texture ID
	 */
	public int getTextureID() {
		return textureID;
	}
	
	

}
