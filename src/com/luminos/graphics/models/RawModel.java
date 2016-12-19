package com.luminos.graphics.models;

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
	private String id;
	
	/**
	 * Constructor used to hold data for rendering
	 * 
	 * @param vaoID			GPU vertex array object pointer
	 * @param vertexCount	Vertex count of model
	 * @param mesh			{@link Mesh} of model
	 * @param id			File location
	 */
	public RawModel(int vaoID, int vertexCount, Mesh mesh, String id){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.mesh = mesh;
		this.id = id;
	}
	
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
	
	/**
	 * Gets the file ID of the mesh
	 * 
	 * @return	file ID of the mesh
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Sets the file ID of the mesh
	 * 
	 * @param id	ID of the mesh
	 */
	public void setID(String id) {
		this.id = id;
	}

}
