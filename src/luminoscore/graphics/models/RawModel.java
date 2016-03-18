package luminoscore.graphics.models;

public class RawModel {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Constructor Fields
	private int vaoID, vertexCount;

	/*
	 * @param vaoID holds the GPU's ID for a model's vao
	 * @param vertexCount holds the number of vertices an object has
	 * 
	 * Constructor
	 */
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	//Getter methods
	public int getID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}

}
