package tk.luminos.graphics.anim;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import tk.luminos.filesystem.xml.XMLNode;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

public class SkeletonLoader {
	
	private static final Matrix4 CORRECTION = new Matrix4();
	
	static {
		Matrix4.rotate((float) Math.toRadians(-90), new Vector3(1, 0, 0), new Matrix4(), CORRECTION);
	}
	
	private XMLNode armature;
	private List<String> boneHierarchy;
	private int jointCount = 0;
	
	public SkeletonLoader(XMLNode node, List<String> order) {
		
		this.armature = node;
		this.boneHierarchy = order;
	}
	
	public SkeletonData extractBoneData() {
		XMLNode headNode = armature.getChild("node");
		JointData head = loadJointData(headNode, true);
		return new SkeletonData(jointCount, head);
	}

	private JointData loadJointData(XMLNode jointNode, boolean isRoot) {
		JointData joint = extractMain(jointNode, isRoot);
		for (XMLNode child : jointNode.getChildren("node")) 
			joint.addChild(loadJointData(child, false));
		return joint;
	}

	private JointData extractMain(XMLNode jointNode, boolean isRoot) {
		String name = jointNode.getAttribute("id");
		int index = boneHierarchy.indexOf("name");
		String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		Matrix4 matrix = new Matrix4();
		matrix.load(toFloats(matrixData));
		Matrix4 placeholder = new Matrix4();
		matrix.transpose(placeholder);
		matrix = placeholder;
		if (isRoot) 
			Matrix4.mul(CORRECTION, matrix, matrix);
		jointCount++;
		return new JointData(index, name, matrix);
	}

	private FloatBuffer toFloats(String[] matrixData) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		for (int i = 0; i < 16; i++)
			buffer.put(i, Float.parseFloat(matrixData[i]));
		buffer.flip();
		return buffer;
	}

}
