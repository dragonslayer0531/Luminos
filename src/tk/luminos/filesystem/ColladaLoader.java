package tk.luminos.filesystem;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import tk.luminos.filesystem.xml.XMLNode;
import tk.luminos.filesystem.xml.XMLParser;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;
import tk.luminos.utilities.File;

/**
 * 
 * Loads Collada (DAE) files to the engine for usage.  It loads the 
 * controllers, visual scenes, animation, and geometry libraries.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ColladaLoader {
	
	/**
	 * Loads the controllers, scene, and geometry to a AnimatedModelData
	 * 
	 * @param file			File to load animation from
	 * @param maxWeights	Maximum weight of the vertices
	 * @return				Model data containing the joints and mesh data
	 * @throws Exception	Thrown if file cannot be found
	 */
	public static AnimatedModelData loadColladaModel(File file, int maxWeights) throws Exception {
		XMLNode node = XMLParser.loadXMLFile(file);
		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();
		
		JointsLoader jointsLoader = new JointsLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		JointsData jointsData = jointsLoader.extractBoneData();
		
		GeometryLoader geometryLoader = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = geometryLoader.extractModelData();
		
		return new AnimatedModelData(jointsData, meshData);
	}
	
	/**
	 * Loads the collada animation to an AnimationData object
	 * 
	 * @param colladaFile		File to be loaded
	 * @return					Data containing information on animation	
	 * @throws Exception		Thrown if file cannot be found
	 */
	public static AnimationData loadColladaAnimation(File colladaFile) throws Exception {
		XMLNode node = XMLParser.loadXMLFile(colladaFile);
		AnimationLoader a = new AnimationLoader(node.getChild("library_animations"));
		AnimationData animData = a.extractAnimation();
		return animData;
	}

}

class AnimationLoader {
	
	private XMLNode animData;
	
	public AnimationLoader(XMLNode animationData) {
		this.animData = animationData;
	}
	
	public AnimationData extractAnimation() {
		float[] times = getKeyTimes();
		float duration = times[times.length-1];
		KeyFrameData[] keyFrames = initKeyFrames(times);
		List<XMLNode> animationNodes = animData.getChildren("animation");
		for (XMLNode jointNode : animationNodes) {
			loadJointTransforms(keyFrames, jointNode);
		}
		return new AnimationData(duration, keyFrames);
	}
	
	private float[] getKeyTimes() {
		XMLNode timeData = animData.getChild("animation").getChild("source").getChild("float_array");
		String[] rawTimes = timeData.getData().split(" ");
		float[] times = new float[rawTimes.length];
		for (int i = 0; i < rawTimes.length; i++)
			times[i] = Float.parseFloat(rawTimes[i]);
		return times;
	}
	
	private KeyFrameData[] initKeyFrames(float[] times) {
		KeyFrameData[] frames = new KeyFrameData[times.length];
		for (int i=0;i<frames.length;i++) {
			frames[i] = new KeyFrameData(times[i]);
		}
		return frames;
	}
	
	private void loadJointTransforms(KeyFrameData[] frames, XMLNode jointData) {
		String jointNameId = getJointName(jointData);
		String dataId = getDataId(jointData);
		XMLNode transformData = jointData.getChildWithAttribute("source", "id", dataId);
		String[] rawData = transformData.getChild("float_array").getData().split(" ");
		processTransforms(jointNameId, rawData, frames);
	}
	
	private String getDataId(XMLNode jointData) {
		XMLNode node = jointData.getChild("sampler").getChildWithAttribute("input", "semantic", "OUTPUT");
		return node.getAttribute("source").substring(1);
	}
	
	private String getJointName(XMLNode jointData) {
		XMLNode channelNode = jointData.getChild("channel");
		String data = channelNode.getAttribute("target");
		return data.split("/")[0];
	}
	
	private void processTransforms(String jointName, String[] rawData, KeyFrameData[] keyFrames) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		float[] matrixData = new float[16];
		for (int i=0;i<keyFrames.length;i++) {
			for (int j=0;j<16;j++) {
				matrixData[j] = Float.parseFloat(rawData[i*16 + j]);
			}
			buffer.clear();
			buffer.put(matrixData);
			buffer.flip();
			Matrix4 transform = new Matrix4();
			transform.load(buffer);
			transform = Matrix4.transpose(transform, null);
			keyFrames[i].addJointTransform(new JointTransformData(jointName, transform));
		}
	}

}

class AnimationData {
	
	public final float lengthSeconds;
	public final KeyFrameData[] keyFrameData;
	
	public AnimationData(float lengthSeconds, KeyFrameData[] keyFrameData) {
		this.lengthSeconds = lengthSeconds;
		this.keyFrameData = keyFrameData;
	}

}

class AnimatedModelData {
	
	private final JointsData joints;
	private final MeshData mesh;
	
	public AnimatedModelData(JointsData joints, MeshData mesh) {
		this.joints = joints;
		this.mesh = mesh;
	}
	public JointsData getJoints() {
		return joints;
	}
	public MeshData getMesh() {
		return mesh;
	}
	
}

class GeometryLoader {
	
	private final XMLNode meshData;
	private final List<VertexSkinData> vertexWeights;
	
	private float[] verticesArray;
	private float[] normalsArray;
	private float[] texturesArray;
	private int[] indicesArray;
	private int[] jointIDsArray;
	private float[] weightArray;
	
	List<Vertex> vertices = new ArrayList<Vertex>();
	List<Vector2> textures = new ArrayList<Vector2>();
	List<Vector3> normals = new ArrayList<Vector3>();
	List<Integer> indices = new ArrayList<Integer>();
	
	private Matrix4 correction = Matrix4.rotate((float) Math.toRadians(-90), new Vector3(1, 0, 0), new Matrix4(), null);
	
	public GeometryLoader(XMLNode geometryNode, List<VertexSkinData> vertexWeights) {
		this.vertexWeights = vertexWeights;
		this.meshData = geometryNode.getChild("geometry").getChild("mesh");
	}
	
	public MeshData extractModelData() {
		readRawData();
		assembleVertices();
		removeUnusedVertices();
		initArrays();
		convertDataToArrays();
		convertIndicesListToArray();
		return new MeshData(verticesArray, texturesArray, normalsArray, indicesArray, jointIDsArray, weightArray, 1);
	}
	
	public void print() {
		System.out.println(indices.size() + " texcoords");
		for (int pos : indices) {
			System.out.print(pos + ", ");
		}
	}
	
	private void readRawData() {
		readPositions();
		readNormals();
		readTextureCoords();
	}
	
	private void readPositions() {
		String positionsID = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		XMLNode positionsData = meshData.getChildWithAttribute("source", "id", positionsID);
		int count = Integer.parseInt(positionsData.getAttribute("count"));
		String[] posData = positionsData.getData().split(" ");
		for (int i = 0; i < count / 3; i++) {
			float x = Float.parseFloat(posData[i * 3]);
			float y = Float.parseFloat(posData[i * 3 + 1]);
			float z = Float.parseFloat(posData[i * 3 + 2]);
			Vector4 position = new Vector4(x, y, z, 1);
			Matrix4.transform(correction,  position,  position);
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
			Matrix4.transform(correction, norm, norm);
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
	
	private void assembleVertices() {
		XMLNode poly = meshData.getChild("polylist");
		int typeCount = poly.getChildren("input").size();
		String[] indexData = poly.getChild("p").getData().split(" ");
		for (int i = 0; i < indexData.length / typeCount; i++) {
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
		} 
		else {
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
			weightArray[i * 3] = weights.weights.get(0);
			weightArray[i * 3 + 1] = weights.weights.get(1);
			weightArray[i * 3 + 2] = weights.weights.get(2);

		}
		return furthestPoint;
	}
	
	private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} 
		else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
			} 
			else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}
	
	private void initArrays() {
		this.verticesArray = new float[vertices.size() * 3];
		this.texturesArray = new float[vertices.size() * 2];
		this.normalsArray = new float[vertices.size() * 3];
		this.jointIDsArray = new int[vertices.size() * 3];
		this.weightArray = new float[vertices.size() * 3];
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

class Vertex {
	
	private static final int NO_INDEX = -1;
	
	private Vector3 position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3> tangents = new ArrayList<Vector3>();
	private Vector3 averagedTangent = new Vector3(0, 0, 0);
	
	private VertexSkinData weightsData;
	
	public Vertex(int index,Vector3 position, VertexSkinData weightsData) {
		this.index = index;
		this.weightsData = weightsData;
		this.position = position;
		this.length = position.magnitude();
	}

	public VertexSkinData getWeightsData() {
		return weightsData;
	}
	
	public void addTangent(Vector3 tangent) {
		tangents.add(tangent);
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
		return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther) {
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	public Vector3 getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}

class VertexSkinData {
	
	public final List<Integer> jointIDs = new ArrayList<Integer>();
	public final List<Float> weights = new ArrayList<Float>();
	
	protected void addJointEffect(int jointID, float weight) {
		for (int i=0;i<weights.size();i++) {
			if (weight > weights.get(i)) {
				jointIDs.add(i, jointID);
				weights.add(i, weight);
				return;
			}
		}
		jointIDs.add(jointID);
		weights.add(weight);
	}
	
	protected void limitJointNumber(int max) {
		if (jointIDs.size() > max) {
			float[] topWeights = new float[max];
			float total = saveTopWeights(topWeights);
			refillWeightList(topWeights, total);
			removeExcessJointIds(max);
		}else if (jointIDs.size() < max) {
			fillEmptyWeights(max);
		}
	}

	private void fillEmptyWeights(int max) {
		while(jointIDs.size() < max) {
			jointIDs.add(0);
			weights.add(0f);
		}
	}
	
	private float saveTopWeights(float[] topWeightsArray) {
		float total = 0;
		for (int i=0;i<topWeightsArray.length;i++) {
			topWeightsArray[i] = weights.get(i);
			total += topWeightsArray[i];
		}
		return total;
	}
	
	private void refillWeightList(float[] topWeights, float total) {
		weights.clear();
		for (int i=0;i<topWeights.length;i++) {
			weights.add(Math.min(topWeights[i]/total, 1));
		}
	}
	
	private void removeExcessJointIds(int max) {
		while(jointIDs.size() > max) {
			jointIDs.remove(jointIDs.size()-1);
		}
	}

}

class JointTransformData {

	public final String jointNameId;
	public final Matrix4 jointLocalTransform;
	
	protected JointTransformData(String jointNameId, Matrix4 jointLocalTransform) {
		this.jointNameId = jointNameId;
		this.jointLocalTransform = jointLocalTransform;
	}
}

class JointsLoader {
	
	private XMLNode armatureData;
	
	private List<String> boneOrder;
	
	private int jointCount;
	
	public JointsLoader(XMLNode visual, List<String> boneOrder) {
		this.armatureData = visual.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		this.boneOrder = boneOrder;
	}
	
	public JointsData extractBoneData() {
		XMLNode headNode = armatureData.getChild("node");
		JointData headJoint = loadJointData(headNode);
		return new JointsData(jointCount, headJoint);
	}
	
	private JointData loadJointData(XMLNode jointNode) {
		JointData joint = extractMainJointData(jointNode);
		for (XMLNode childNode : jointNode.getChildren("node")) {
			joint.addChild(loadJointData(childNode));
		}
		return joint;
	}
	
	private JointData extractMainJointData(XMLNode jointNode) {
		String nameId = jointNode.getAttribute("id");
		int index = boneOrder.indexOf(nameId);
		String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		Matrix4 matrix = new Matrix4();
		matrix.load(convertData(matrixData));
		Matrix4.transpose(matrix, matrix);
		jointCount++;
		return new JointData(index, nameId, matrix);
	}
	
	private FloatBuffer convertData(String[] rawData) {
		float[] matrixData = new float[16];
		for (int i=0;i<matrixData.length;i++) {
			matrixData[i] = Float.parseFloat(rawData[i]);
		}
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrixData);
		buffer.flip();
		return buffer;
	}

}

class JointsData {
	
	public final int count;
	public final JointData head;
	
	public JointsData(int count, JointData head) {
		this.count = count;
		this.head = head;
	}

}

class JointData {
	
	public final int index;
	public final String name;
	public final Matrix4 bindLocalTransform;
	
	public final List<JointData> children = new ArrayList<JointData>();
	
	public JointData(int index, String name, Matrix4 bindLocalTransform) {
		this.index = index;
		this.name = name;
		this.bindLocalTransform = bindLocalTransform;
	}
	
	public void addChild(JointData joint) {
		this.children.add(joint);
	}

}

class KeyFrameData {
	
	public final float time;
	public final List<JointTransformData> jointTransforms = new ArrayList<JointTransformData>();
	
	protected KeyFrameData(float time) {
		this.time = time;
	}
	
	protected void addJointTransform(JointTransformData transform) {
		jointTransforms.add(transform);
	}

}

class MeshData {
	
	private static final int DIMENSIONS = 3;
	
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private int[] jointIDs;
	private float[] vertexWeights;
	private float furthestPoint;
	
	public MeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, int[] jointIDs,
			float[] vertexWeights, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.jointIDs = jointIDs;
		this.vertexWeights = vertexWeights;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
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

	public float getFurthestPoint() {
		return furthestPoint;
	}
	
	public int getVertexCount() {
		return vertices.length / DIMENSIONS;
	}

}

class SkinLoader {
	
	private final XMLNode skinningData;
	private final int maxWeights;
	
	public SkinLoader(XMLNode controllersNode, int maxWeights) {
		this.skinningData = controllersNode.getChild("controller").getChild("skin");
		this.maxWeights = maxWeights;
	}

	public SkinningData extractSkinData() {
		List<String> jointsList = loadJointsList();
		float[] weights = loadWeights();
		XMLNode weightsDataNode = skinningData.getChild("vertex_weights");
		int[] effectorJointCounts = getEffectiveJointsCounts(weightsDataNode);
		List<VertexSkinData> vertexWeights = getSkinData(weightsDataNode, effectorJointCounts, weights);
		return new SkinningData(jointsList, vertexWeights);
	}

	private List<String> loadJointsList() {
		XMLNode inputNode = skinningData.getChild("vertex_weights");
		String jointDataId = inputNode.getChildWithAttribute("input", "semantic", "JOINT").getAttribute("source")
				.substring(1);
		XMLNode jointsNode = skinningData.getChildWithAttribute("source", "id", jointDataId).getChild("Name_array");
		String[] names = jointsNode.getData().split(" ");
		List<String> jointsList = new ArrayList<String>();
		for (String name : names) {
			jointsList.add(name);
		}
		return jointsList;
	}

	private float[] loadWeights() {
		XMLNode inputNode = skinningData.getChild("vertex_weights");
		String weightsDataId = inputNode.getChildWithAttribute("input", "semantic", "WEIGHT").getAttribute("source")
				.substring(1);
		XMLNode weightsNode = skinningData.getChildWithAttribute("source", "id", weightsDataId).getChild("float_array");
		String[] rawData = weightsNode.getData().split(" ");
		float[] weights = new float[rawData.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Float.parseFloat(rawData[i]);
		}
		return weights;
	}

	private int[] getEffectiveJointsCounts(XMLNode weightsDataNode) {
		String[] rawData = weightsDataNode.getChild("vcount").getData().split(" ");
		int[] counts = new int[rawData.length];
		for (int i = 0; i < rawData.length; i++) {
			counts[i] = Integer.parseInt(rawData[i]);
		}
		return counts;
	}

	private List<VertexSkinData> getSkinData(XMLNode weightsDataNode, int[] counts, float[] weights) {
		String[] rawData = weightsDataNode.getChild("v").getData().split(" ");
		List<VertexSkinData> skinningData = new ArrayList<VertexSkinData>();
		int pointer = 0;
		for (int count : counts) {
			VertexSkinData skinData = new VertexSkinData();
			for (int i = 0; i < count; i++) {
				int jointId = Integer.parseInt(rawData[pointer++]);
				int weightId = Integer.parseInt(rawData[pointer++]);
				skinData.addJointEffect(jointId, weights[weightId]);
			}
			skinData.limitJointNumber(maxWeights);
			skinningData.add(skinData);
		}
		return skinningData;
	}

}

class SkinningData {
	
	public final List<String> jointOrder;
	public final List<VertexSkinData> verticesSkinData;
	
	protected SkinningData(List<String> jointOrder, List<VertexSkinData> verticesSkinData) {
		this.jointOrder = jointOrder;
		this.verticesSkinData = verticesSkinData;
	}

}
