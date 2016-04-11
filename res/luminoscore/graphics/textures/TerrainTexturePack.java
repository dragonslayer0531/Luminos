package luminoscore.graphics.textures;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Texture Pack for Terrains
 *
 */

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	/**
	 * @param backgroundTexture	Color: (0, 0, 0)
	 * @param rTexture			Color: (1, 0, 0)
	 * @param gTexture			Color: (0, 1, 0)
	 * @param bTexture			Color: (0, 0, 1)
	 * 
	 * Constructor
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	/**
	 * @return TerrainTexture	Black texture
	 * 
	 * Gets background texture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	/**
	 * @return TerrainTexture	Red Texture
	 * 
	 * Gets R Texture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	/**
	 * @return	TerrainTexture	Green Texture
	 * 
	 * Gets G Texture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}

	/**
	 * @return	TerrainTexture	Blue Texture
	 * 
	 * Gets B Texture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}


}
