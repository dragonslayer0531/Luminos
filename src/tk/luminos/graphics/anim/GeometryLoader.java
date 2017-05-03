package tk.luminos.graphics.anim;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.filesystem.xml.XMLNode;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;

public class GeometryLoader {

	private static final Matrix4 CORRECTION = new Matrix4();

	static {
		Matrix4.rotate((float) Math.toRadians(-90), new Vector3(1, 0, 0), new Matrix4(), CORRECTION);
	}

	private final XMLNode meshData;
	private final List<VertexSkinData> vertexWeights;
	private float[] verticesArray;
	private float[] normalsArray;
	private float[] texturesArray;
	private int[] indicesArray;
	private int[] jointIDsArray;
	private float[] weightsArray;

	List<Vertex> vertices = new ArrayList<Vertex>();
	List<Vector2> textures = new ArrayList<Vector2>();
	List<Vector3> normals = new ArrayList<Vector3>();
	List<Integer> indices = new ArrayList<Integer>();

	public GeometryLoader(XMLNode geometryNode, List<VertexSkinData> vertexWeights) {
		this.vertexWeights = vertexWeights;
		this.meshData = geometryNode.getChild("geometry").getChild("mesh");
	}

	public MeshData extractModelData(){
		readRawData();
		assembleVertices();
		removeUnusedVertices();
		initArrays();
		convertDataToArrays();
		convertIndicesListToArray();
		return new MeshData(verticesArray, texturesArray, normalsArray, indicesArray, jointIDsArray, weightsArray);
	}

	private void readRawData() {
		readPositions();
		readNormals();
		readTextureCoords();
	}

	private void readPositions() {
		String positionsId = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		XMLNode positionsData = meshData.getChildWithAttribute("source", "id", positionsId).getChild("float_array");
		int count = Integer.parseInt(positionsData.getAttribute("count"));
		String[] posData = positionsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			float x = Float.parseFloat(posData[i * 3]);
			float y = Float.parseFloat(posData[i * 3 + 1]);
			float z = Float.parseFloat(posData[i * 3 + 2]);
			Vector4 position = new Vector4(x, y, z, 1);
			Matrix4.transform(CORRECTION, position, position);
			vertices.add(new Vertex(vertices.size(), new Vector3(position.x, position.y, position.z), vertexWeights.get(vertices.size())));
		}
	}

	private void readNormals() {
		String normalsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "NORMAL")
				.getAttribute("source").substring(1);
		XMLNode normalsData = meshData.getChildWithAttribute("source", "id", normalsId).getChild("float_array");
		int count = Integer.parseInt(normalsData.getAttribute("count"));
		String[] normData = normalsData.getData().split(" ");
		for (int i = 0; i < count/3; i++) {
			float x = Float.parseFloat(normData[i * 3]);
			float y = Float.parseFloat(normData[i * 3 + 1]);
			float z = Float.parseFloat(normData[i * 3 + 2]);
			Vector4 norm = new Vector4(x, y, z, 0f);
			Matrix4.transform(CORRECTION, norm, norm);
			normals.add(new Vector3(norm.x, norm.y, norm.z));
		}
	}

	private void readTextureCoords() {
		String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "TEXCOORD")
				.getAttribute("source").substring(1);
		XMLNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
		int count = Integer.parseInt(texCoordsData.getAttribute("count"));
		String[] texData = texCoordsData.getData().split(" ");
		for (int i = 0; i < count/2; i++) {
			float s = Float.parseFloat(texData[i * 2]);
			float t = Float.parseFloat(texData[i * 2 + 1]);
			textures.add(new Vector2(s, t));
		}
	}

	private void assembleVertices(){
		XMLNode poly = meshData.getChild("polylist");
		int typeCount = poly.getChildren("input").size();
		String[] indexData = poly.getChild("p").getData().split(" ");
		for(int i=0;i<indexData.length/typeCount;i++){
			int positionIndex = Integer.parseInt(indexData[i * typeCount]);
			int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
			int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
			processVertex(positionIndex, normalIndex, texCoordIndex);
		}
	}


	private Vertex processVertex(int posIndex, int normIndex, int texIndex) {
		Vertex currentVertex = vertices.get(posIndex);
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(texIndex);
			currentVertex.setNormalIndex(normIndex);
			indices.add(posIndex);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, texIndex, normIndex);
		}
	}

	private int[] convertIndicesListToArray() {
		this.indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	private float convertDataToArrays() {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector3 position = currentVertex.getPosition();
			Vector2 textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3 normalVector = normals.get(currentVertex.getNormalIndex());
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			VertexSkinData weights = currentVertex.getWeightsData();
			jointIDsArray[i * 3] = weights.jointIDs.get(0);
			jointIDsArray[i * 3 + 1] = weights.jointIDs.get(1);
			jointIDsArray[i * 3 + 2] = weights.jointIDs.get(2);
			weightsArray[i * 3] = weights.weights.get(0);
			weightsArray[i * 3 + 1] = weights.weights.get(1);
			weightsArray[i * 3 + 2] = weights.weights.get(2);

		}
		return furthestPoint;
	}

	private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicate(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}

	private void initArrays(){
		this.verticesArray = new float[vertices.size() * 3];
		this.texturesArray = new float[vertices.size() * 2];
		this.normalsArray = new float[vertices.size() * 3];
		this.jointIDsArray = new int[vertices.size() * 3];
		this.weightsArray = new float[vertices.size() * 3];
	}

	private void removeUnusedVertices() {
		for (Vertex vertex : vertices) {
			vertex.averageTangents();
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}

}
