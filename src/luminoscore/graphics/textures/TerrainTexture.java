package luminoscore.graphics.textures;

public class TerrainTexture {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Constructor fields
	private int vao;
	
	/*
	 * @param vao Holds the GPU VAO ID
	 * 
	 * Constructor
	 */	
	public TerrainTexture(int vao) {
		this.vao = vao;
	}
	
	//Getter Method
	public int getTextureID() {
		return vao;
	}

}
