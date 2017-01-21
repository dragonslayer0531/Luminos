package tk.luminos.graphics.models;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Wraps data for each vertex
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Vertex {

	private static final int NO_INDEX = -1;

	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);

	/**
	 * Constructor
	 * 
	 * @param index			Vertex index position
	 * @param position		Position of vertex
	 */
	public Vertex(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.magnitude();
	}

	/**
	 * Adds tangent to vertex
	 * 
	 * @param tangent	Tangent to add
	 */
	public void addTangent(Vector3f tangent){
		tangents.add(tangent);
	}

	/**
	 * Averages all attached tangents
	 */
	public void averageTangents(){
		if(tangents.isEmpty()){
			return;
		}
		for(Vector3f tangent : tangents){
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalize();
	}

	/**
	 * Gets the average of tangents
	 * 
	 * @return		Average tangent
	 */
	public Vector3f getAverageTangent(){
		return averagedTangent;
	}

	/**
	 * Gets the index position
	 * 
	 * @return		Index position
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * Gets length of vertex from the origin
	 * 
	 * @return		Distance of vertex from origin
	 */
	public float getLength(){
		return length;
	}

	/**
	 * Checks if the index for texture and normal is set
	 * 
	 * @return		If boolean is set
	 */
	public boolean isSet(){
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}

	/**
	 * Checks if texture and normal are same
	 * 
	 * @param textureIndexOther		Other texture index
	 * @param normalIndexOther		Other normal index
	 * @return		If texture and normal are same as evaluated parameters
	 */
	public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}

	/**
	 * Sets texture index
	 * 
	 * @param textureIndex		Index of texture
	 */
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}

	/**
	 * Sets normal index
	 * 
	 * @param normalIndex		Index of normal
	 */
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}

	/**
	 * Gets position of vertex
	 * 
	 * @return	Vertex position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Gets texture index
	 * 
	 * @return	Texture index
	 */
	public int getTextureIndex() {
		return textureIndex;
	}

	/**
	 * Gets normal index
	 * 
	 * @return	Normal index
	 */
	public int getNormalIndex() {
		return normalIndex;
	}

	/**
	 * Gets a duplicate vertex
	 * 
	 * @return	duplicate vertex
	 */
	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	/**
	 * Set the duplicate vertex
	 * 
	 * @param duplicateVertex		Duplicated vertex
	 */
	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}


}
