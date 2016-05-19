package tk.luminos.luminoscore.graphics.text;

/**
 * 
 * Text Mesh Data of File
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class TextMeshData {

	private float[] vertexPositions;
    private float[] textureCoords;
     
    /**
     * Constructor
     * 
     * @param vertexPositions	Vertex positions of mesh
     * @param textureCoords		Texture coordinates of mesh
     */
    protected TextMeshData(float[] vertexPositions, float[] textureCoords){
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
    }
 
    /**
     * Gets vertex positions
     * 
     * @return Vertex Positions
     */
    public float[] getVertexPositions() {
        return vertexPositions;
    }
 
    /**
     * Gets texture coordinates
     * 
     * @return Texture Coordinates
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    /**
     * Gets vertex count
     * 
     * @return Vertex Count
     */
    public int getVertexCount() {
        return vertexPositions.length/2;
    }
	
}
