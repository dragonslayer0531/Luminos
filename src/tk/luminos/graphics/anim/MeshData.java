package tk.luminos.graphics.anim;

public class MeshData {
	
	private static final int DIM = 3;
	
	private float[] vertices;
	private float[] texture;
	private float[] normals;
	private int[] indices;
	private int[] jointIDs;
	private float[] vertexWeights;
	
	public MeshData(float[] vertices, float[] texture, float[] normals, int[] indices, int[] jointIDs,
			float[] vertexWeights) {
		this.vertices = vertices;
		this.texture = texture;
		this.normals = normals;
		this.indices = indices;
		this.jointIDs = jointIDs;
		this.vertexWeights = vertexWeights;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTexture() {
		return texture;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public int[] getJointIDs() {
		return jointIDs;
	}

	public float[] getVertexWeights() {
		return vertexWeights;
	}
	
	public int getVertexCount() {
		return vertices.length / DIM;
	}

}
