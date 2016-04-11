package luminoscore.graphics.models;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 *
 * Class that wraps the VAO ID from the GPU and the objects Vertex count
 */

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	
	/**
	 * @param vaoID			GPU vertex array object pointer
	 * @param vertexCount	Vertex count of model
	 * 
	 * Constructor used to hold data for rendering
	 */
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * @return int	GPU VAO pointer
	 * 
	 * Gets the ID of the VAO
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return int 	Vertex Count
	 * 
	 * Gets the vertex count of the model
	 */
	public int getVertexCount() {
		return vertexCount;
	}

}
