package luminoscore.graphics.text;

public class TextMeshData {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 * Adapted From: ThinMatrix
	 */
	
	//Constructor Fields
	private float[] vertexPositions;
	private float[] textureCoords;
	
	/*
	 * @param vertexPositions Stores vertex positions
	 * @param textureCoords Stores texture coordinates
	 * 
	 * Constructor
	 */
	public TextMeshData(float[] vertexPositions, float[] textureCoords) {
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
	}
	
	//Getter Methods
	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}
	
	public int getVertexCount() {
		return vertexPositions.length / 2;
	}

}
