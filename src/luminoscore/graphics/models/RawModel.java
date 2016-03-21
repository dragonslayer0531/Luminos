package luminoscore.graphics.models;

import luminoscore.physics.collisions.collider.AABB;

public class RawModel {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Constructor Fields
	private int vaoID, vertexCount;
	private AABB aabb;

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

	/*
	 * @param vaoID holds the GPU's ID for a model's vao
	 * @param vertexCount holds the number of vertices an object has
	 * @param aabb defines the model's axis aligned bounding box
	 * 
	 * Constructor
	 */	
	public RawModel(int vaoID, int vertexCount, AABB aabb) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.aabb = aabb;
	}
	
	//Getter methods
	public int getID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public AABB getAABB() {
		return aabb;
	}

}
