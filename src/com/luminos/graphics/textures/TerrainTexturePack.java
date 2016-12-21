package com.luminos.graphics.textures;

/**
 * 
 * Texture Pack for Terrains
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	/**
	 * Constructor
	 * 
	 * @param backgroundTexture	Color: (0, 0, 0)
	 * @param rTexture			Color: (1, 0, 0)
	 * @param gTexture			Color: (0, 1, 0)
	 * @param bTexture			Color: (0, 0, 1)
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	/**
	 * Gets background texture
	 * 
	 * @return	Black texture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	/**
	 * Gets R Texture
	 * 
	 * @return	Red Texture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	/**
	 * Gets G Texture
	 * 
	 * @return	Green Texture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}

	/**
	 * Gets B Texture
	 * 
	 * @return	Blue Texture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}

}
