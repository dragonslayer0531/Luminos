package com.luminos.graphics.models;

import java.util.ArrayList;
import java.util.List;

import com.luminos.maths.vector.Vector3f;

/**
 * 
 * Contains the mesh data of the model
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Mesh {
	
	private float[] vertices;
	private float[] normals;

	/**
	 * Constructor
	 * 
	 * @param vertices	Vertex positions
	 * @param normals	Normal vectors
	 */
	public Mesh(float[] vertices, float[] normals) {
		this.vertices = vertices;
		this.normals = normals;
	}

	/**
	 * Gets the vertices of the model
	 * 
	 * @return Positions of vertices of model
	 */
	public List<Vector3f> getVertices() {
		List<Vector3f> ret = new ArrayList<Vector3f>();
		Vector3f placeHolder = new Vector3f();
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i] % 3 == 1) {
				placeHolder.x = vertices[i];
			} else if(vertices[i] % 3 == 2) {
				placeHolder.y = vertices[i];
			} else {
				placeHolder.z = vertices[i];
				ret.add(placeHolder);
				placeHolder = new Vector3f();
			}
		}
		return ret;
	}
	
	/**
	 * Gets the normals of the model\
	 * 
	 * @return Normals of the vertices of model
	 */
	public List<Vector3f> getNormals() {
		List<Vector3f> ret = new ArrayList<Vector3f>();
		Vector3f placeHolder = new Vector3f();
		for(int i = 0; i < normals.length; i++) {
			if(normals[i] % 3 == 1) {
				placeHolder.x = normals[i];
			} else if(normals[i] % 3 == 2) {
				placeHolder.y = normals[i];
			} else {
				placeHolder.z = normals[i];
				ret.add(placeHolder);
				placeHolder = new Vector3f();
			}
		}
		return ret;
	}

}
