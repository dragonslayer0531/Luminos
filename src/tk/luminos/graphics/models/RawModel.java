package tk.luminos.graphics.models;

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
	
	/**
	 * Constructor used to hold data for rendering
	 * 
	 * @param vaoID			GPU vertex array object pointer
	 * @param vertexCount	Vertex count of model
	 */
	public RawModel(int vaoID, int vertexCount){
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
	 * Checks raw models for equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof RawModel)) 
			return false;
		RawModel other = (RawModel) obj;
		return vaoID == other.vaoID;
	}

}
