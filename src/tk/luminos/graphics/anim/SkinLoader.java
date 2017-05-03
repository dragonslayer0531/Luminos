package tk.luminos.graphics.anim;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.filesystem.xml.XMLNode;

public class SkinLoader {
	
	private final XMLNode skinningData;
	private final int maxWeights;
	
	public SkinLoader(XMLNode controllers, int maxWeights) {
		this.skinningData = controllers.getChild("controller").getChild("skin");
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
		XMLNode input = skinningData.getChild("vertex_weights");
		String jointDataID = input.getChildWithAttribute("input", "semantic", "JOINT").getAttribute("source").substring(1);
		XMLNode joints = skinningData.getChildWithAttribute("source", "id", jointDataID).getChild("Name_array");
		String[] names = joints.getData().split(" ");
		List<String> jointsList = new ArrayList<String>();
		for (String name : names)
			jointsList.add(name);
		return jointsList;
	}
	
	private float[] loadWeights() {
		XMLNode input = skinningData.getChild("vertex_weights");
		String weightsDataID = input.getChildWithAttribute("input", "semantic", "WEIGHT").getAttribute("SOURCE").substring(1);
		XMLNode weightsNode = skinningData.getChildWithAttribute("source", "id", weightsDataID).getChild("float_array");
		String[] rawData = weightsNode.getData().split(" ");
		float[] weights = new float[rawData.length];
		for (int i = 0; i < weights.length; i++)
			weights[i] = Float.parseFloat(rawData[i]);
		return weights;
	}
	
	private int[] getEffectiveJointsCounts(XMLNode weightsDataNode) {
		String[] rawData = weightsDataNode.getChild("vcount").getData().split(" ");
		int[] counts = new int[rawData.length];
		for (int i = 0; i < rawData.length; i++)
			counts[i] = Integer.parseInt(rawData[i]);
		return counts;
	}
	
	private List<VertexSkinData> getSkinData(XMLNode weightsDataNode, int[] counts, float[] weights) {
		String[] rawData = weightsDataNode.getChild("v").getData().split(" ");
		List<VertexSkinData> skinningData = new ArrayList<VertexSkinData>();
		int pointer = 0;
		for (int count : counts) {
			VertexSkinData skinData = new VertexSkinData();
			for (int i = 0; i < count; i++) {
				int jointID = Integer.parseInt(rawData[pointer++]);
				int weightID = Integer.parseInt(rawData[pointer++]);
				skinData.addJointEffect(jointID, weights[weightID]);
			}
			skinData.limitJointNumber(maxWeights);
			skinningData.add(skinData);
		}
		return skinningData;
	}

}
