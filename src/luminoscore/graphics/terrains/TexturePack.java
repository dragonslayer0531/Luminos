package luminoscore.graphics.terrains;

import luminoscore.graphics.loaders.ImageLoader;
import luminoscore.graphics.textures.TerrainTexture;

public class TexturePack {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Constructor Fields
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	private TerrainTexture nTexture;
	private TerrainTexture blendMap;
	
	/*
	 * @param rTexture Defines texture mapped to R Value of blend map
	 * @param gTexture Defines texture mapped to G Value of blend map
	 * @param bTexture Defines texture mapped to B Value of blend map
	 * @param nTexture Defines texture mapped to Black Value of blend map
	 * @param blendMap Defines blend map
	 * 
	 * Constructor
	 */
	public TexturePack(TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture, TerrainTexture nTexture, TerrainTexture blendMap) {
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
		this.nTexture = nTexture;
		this.blendMap = blendMap;
	}
	
	/*
	 * @param rString Defines file location of R Texture
	 * @param gString Defines file location of G Texture
	 * @param bString Defines file location of B Texture
	 * @param nString Defines file location of Black Texture
	 * @param blendString Defines file location of Blend Map
	 * @param loader Defines what image loader to use
	 * @param mipmap Determines whether to use mipmapping 
	 * 
	 * Alternate Constructor
	 */
	public TexturePack(String rString, String gString, String bString, String nString, String blendString, ImageLoader loader, boolean mipmap) {
		this.rTexture = new TerrainTexture(loader.loadTexture(rString, mipmap));
		this.gTexture = new TerrainTexture(loader.loadTexture(gString, mipmap));
		this.bTexture = new TerrainTexture(loader.loadTexture(bString, mipmap));
		this.nTexture = new TerrainTexture(loader.loadTexture(nString, mipmap));
		this.blendMap = new TerrainTexture(loader.loadTexture(blendString, mipmap));
	}

	//Getter methods
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}

	public TerrainTexture getnTexture() {
		return nTexture;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

}
