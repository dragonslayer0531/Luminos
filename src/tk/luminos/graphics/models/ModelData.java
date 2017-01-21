package tk.luminos.graphics.models;

/**
 * 
 * Holds data about the model
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
	 * @param vertices			Vertex Positions
	 * @param textureCoords		Texture Coordinates
	 * @param normals			Normal Vectors
	 * @param tangents			Tangent Vectors
	 * @param indices			Index array
	 * @param furthestPoint		Furthest point from center
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
	 * Gets vertex array
	 * 
	 * @return		Vertex array
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * Gets texture coordinates
	 * 
	 * @return		Texture coordinates
	 */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/**
	 * Gets tangent vectors
	 * 
	 * @return		Tangent vectors
	 */
	public float[] getTangents(){
		return tangents;
	}

	/**
	 * Gets normal vectors
	 * 
	 * @return		Normal vectors
	 */
	public float[] getNormals() {
		return normals;
	}

	/**
	 * Gets index arrays
	 * 
	 * @return		Index arrays
	 */
	public int[] getIndices() {
		return indices;
	}

	/**
	 * Gets furthest point from center
	 * 
	 * @return		Furthest point
	 */
	public float getFurthestPoint() {
		return furthestPoint;
	}

}
