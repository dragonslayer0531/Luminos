package tk.luminos.graphics.opengl.fileloaders;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.maths.vector.Vector3f;

/**
 * Class containing vertex data
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
     * @param index		Index of vertex
     * @param position	Position of vertex
     */
    public Vertex(int index,Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.magnitude();
    }
     
    /**
     * Adds tangent to vertex
     * 
     * @param tangent		Tangent to add
     */
    public void addTangent(Vector3f tangent){
        tangents.add(tangent);
    }
     
    /**
     * Averages the tangents
     */
    public void averageTangents(){
        if(tangents.isEmpty()){
            return;
        }
        for(Vector3f tangent : tangents){
            Vector3f.add(averagedTangent, tangent, averagedTangent);
        }
        averagedTangent.normalise();
    }
     
    /**
     * Gets the averaged tangent
     * 
     * @return	avereaged tangent
     */
    public Vector3f getAverageTangent(){
        return averagedTangent;
    }
     
    /**
     * Gets the index of the vertex
     * 
     * @return	index of the vertex
     */
    public int getIndex(){
        return index;
    }
     
    /**
     * Gets the length of the vertex
     * 
     * @return	length of the vertex
     */
    public float getLength(){
        return length;
    }
     
    /**
     * Gets if the vertex has been set
     * 
     * @return	if the vertex has been set
     */
    public boolean isSet(){
        return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
    }
     
    /**
     * Gets if the texture and normal index are the same
     * 
     * @param textureIndexOther		Texture index
     * @param normalIndexOther		Normal index
     * @return	if the texture and normal index are the same
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
        return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
    }
     
    /**
     * Sets the texture index
     * 
     * @param textureIndex		texture index
     */
    public void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }
     
    /**
     * Sets the normal index
     * 
     * @param normalIndex		normal index
     */
    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }
 
    /**
     * Gets the position of the vertex
     * 
     * @return	position of the vertex
     */
    public Vector3f getPosition() {
        return position;
    }
 
    /**
     * Gets the texture index
     * 
     * @return	texture index
     */
    public int getTextureIndex() {
        return textureIndex;
    }
 
    /**
     * Gets the normal index
     * 
     * @return	normal index
     */
    public int getNormalIndex() {
        return normalIndex;
    }
 
    /**
     * Gets the duplicate vertex
     * 
     * @return	duplicate vertex
     */
    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }
 
    /**
     * Sets the duplicate vertex
     * 
     * @param duplicateVertex		Duplicated vertex
     */
    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }


}
