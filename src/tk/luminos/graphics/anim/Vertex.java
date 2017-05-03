package tk.luminos.graphics.anim;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.maths.Vector3;

public class Vertex {
	
	private static final int NO_INDEX = -0x1;
	
	private Vector3 position;
	private int texture = NO_INDEX;
	private int normal = NO_INDEX;
	private Vertex duplicate = null;
	private int index;
	private float length;
	private List<Vector3> tangents = new ArrayList<Vector3>();
	private Vector3 averagedTangent = new Vector3(0, 0, 0);
	
	private VertexSkinData weightsData;
	
	public Vertex(int index, Vector3 position, VertexSkinData weightsData) {
		this.index = index;
		this.weightsData = weightsData;
		this.position = position;
		this.length = position.magnitude();
	}
	
	public VertexSkinData getWeightsData() {
		return this.weightsData;
	}
	
	public void addTangent(Vector3 tan) {
		this.tangents.add(tan);
	}
	
	public void averageTangents() {
		if (tangents.isEmpty())
			return;
		for (Vector3 tangent : tangents)
			Vector3.add(averagedTangent, tangent, averagedTangent);
		averagedTangent.normalize();
	}
	
	public Vector3 getAverageTangent() {
		return averagedTangent;
	}
	
	public int getIndex() {
		return index;
	}
	
	public float getLength() {
		return length;
	}
	
	public boolean isSet() {
		return texture != NO_INDEX && normal != NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndex, int normalIndex) {
		return texture == textureIndex && normal == normalIndex;
	}
	
	public void setTextureIndex(int ind) {
		this.texture = ind;
	}
	
	public int getTextureIndex() {
		return texture;
	}
	
	public void setNormalIndex(int ind) {
		this.normal = ind;
	}
	
	public int getNormalIndex() {
		return normal;
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public Vertex getDuplicateVertex() {
		return duplicate;
	}
	
	public void setDuplicate(Vertex dup) {
		this.duplicate = dup;
	}

}
