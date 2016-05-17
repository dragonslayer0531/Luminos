package luminoscore.graphics.text;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Text Mesh Data of File
 *
 */

public class TextMeshData {

	private float[] vertexPositions;
    private float[] textureCoords;
     
    /**
     * @param vertexPositions	Vertex positions of mesh
     * @param textureCoords		Texture coordinates of mesh
     * 
     * Constructor
     */
    protected TextMeshData(float[] vertexPositions, float[] textureCoords){
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
    }
 
    /**
     * @return float[]	Vertex Positions
     * 
     * Gets vertex positions
     */
    public float[] getVertexPositions() {
        return vertexPositions;
    }
 
    /**
     * @return float[]	Texture Coordinates
     * 
     * Gets texture coordinates
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    /**
     * @return int	Vertex Count
     * 
     * Gets vertex count
     */
    public int getVertexCount() {
        return vertexPositions.length/2;
    }
	
}
