package luminoscore.graphics.textures;

/**
 * 
 * Texture to use for particle
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ParticleTexture {

	private int textureID;
	private int numberOfRows;
	
	/**
	 * Constructor
	 * 
	 * @param textureID			GPU ID of texture
	 * @param numberOfRows		Number of rows in texture
	 */
	public ParticleTexture(int textureID, int numberOfRows) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
	}

	/**
	 * Gets the GPU ID of texture
	 * 
	 * @return	GPU ID of texture
	 */
	public int getTextureID() {
		return textureID;
	}

	/**
	 * Gets the number of rows in the texture
	 * 
	 * @return Number of rows in the texture
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

}
