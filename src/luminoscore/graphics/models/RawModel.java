package luminoscore.graphics.models;

/**
 * 
 * Class that wraps the VAO ID from the GPU and the objects Vertex count
 *
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	private Mesh mesh;
	
	/**
	 * Constructor used to hold data for rendering
	 * 
	 * @param vaoID			GPU vertex array object pointer
	 * @param vertexCount	Vertex count of model
	 * @param mesh			{@link Mesh} of model
	 */
	public RawModel(int vaoID, int vertexCount, Mesh mesh){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.mesh = mesh;
	}
	
	/**
	 * Constructor used to hold data for rendering
	 * 
	 * @param vaoID			GPU vertex array object pointer
	 * @param vertexCount	Vertex count of model
	 */ 
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;		
	}

	/**
	 * Gets the ID of the VAO
	 * 
	 * @return GPU VAO pointer
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Gets the vertex count of the model
	 * 
	 * @return Vertex Count
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	
	/**
	 * Gets the {@link Mesh} of the model
	 * 
	 * @return mesh of model
	 */
	public Mesh getMesh() {
		return mesh;
	}

}
