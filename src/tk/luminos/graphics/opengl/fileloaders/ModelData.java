package tk.luminos.graphics.opengl.fileloaders;

/**
 * Class containing model data of raw models
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class ModelData {
	 
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;
    private int[] indices;
    private float furthestPoint;
 
    /**
     * Constructor
     * 
     * @param vertices			Vertex arrays
     * @param textureCoords		Texture coordinates
     * @param normals			Normal arrays
     * @param tangents			Tangent arrays
     * @param indices			Indices arrays
     * @param furthestPoint		Farthest point of model
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.tangents = tangents;
    }
 
    /**
     * Gets the vertex array
     * 
     * @return	vertex array
     */
    public float[] getVertices() {
        return vertices;
    }
 
    /**
     * Gets the textured coordinates array
     * 
     * @return	textured coordinates array
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }
     
    /**
     * Gets the tangents array
     * 
     * @return	tangents array
     */
    public float[] getTangents(){
        return tangents;
    }
 
    /**
     * Gets the normals array
     * 
     * @return	normals array
     */
    public float[] getNormals() {
        return normals;
    }
 
    /**
     * Gets the indices array
     * 
     * @return	indices array
     */
    public int[] getIndices() {
        return indices;
    }
 
    /**
     * Gets the farthest point of the model
     * 
     * @return	Farthest point of the model
     */
    public float getFurthestPoint() {
        return furthestPoint;
    }
 
}
