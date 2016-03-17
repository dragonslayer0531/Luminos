package luminoscore.graphics.models;

import java.util.List;

import luminoscore.graphics.loaders.VAOLoader;
import luminoscore.util.math.Vector3f;

public class TexturelessModel {
	
	private List<Integer> indices;
	private List<Vector3f> vertices, normals;
	private int dimensions;
	
	private int[] indicesArray;
	private float[] verticesArray, normalsArray, texturesArray;
	
	public TexturelessModel(List<Integer> indices, List<Vector3f> vertices, List<Vector3f> normals,
			int dimensions) {
		this.indices = indices;
		this.vertices = vertices;
		this.normals = normals;
		this.dimensions = dimensions;
	}
	
	public void loadTextureArray(float[] texturesArray) {
		this.texturesArray = texturesArray;
		calculate();
	}
	
	private void calculate() {
		verticesArray = new float[vertices.size() * 3];
		int vPointer = 0;
		for(Vector3f vertex : vertices) {
			verticesArray[vPointer * 3] = vertex.x;
			verticesArray[vPointer * 3 + 1] = vertex.y;
			verticesArray[vPointer * 3 + 2] = vertex.z;
			vPointer++;
		}
		
		float[] normalsArray = new float[normals.size() * 3];
		int nPointer = 0;
		for(Vector3f normal : normals) {
			normalsArray[nPointer * 3] = normal.x;
			normalsArray[nPointer * 3 + 1] = normal.y;
			normalsArray[nPointer * 3 + 2] = normal.z;
			nPointer++;
		}
		
		int[] indicesArray = new int[indices.size()];
		int iPointer = 0;
		for(Integer indice : indices) {
			indicesArray[iPointer] = indice;
			iPointer++;
		}
	}
	
	public RawModel getRawModel(VAOLoader loader) {
		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray, 3);
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int[] getIndicesArray() {
		return indicesArray;
	}

	public void setIndicesArray(int[] indicesArray) {
		this.indicesArray = indicesArray;
	}

	public float[] getVerticesArray() {
		return verticesArray;
	}

	public void setVerticesArray(float[] verticesArray) {
		this.verticesArray = verticesArray;
	}

	public float[] getNormalsArray() {
		return normalsArray;
	}

	public void setNormalsArray(float[] normalsArray) {
		this.normalsArray = normalsArray;
	}

	public float[] getTexturesArray() {
		return texturesArray;
	}

	public void setTexturesArray(float[] texturesArray) {
		this.texturesArray = texturesArray;
	}

}
