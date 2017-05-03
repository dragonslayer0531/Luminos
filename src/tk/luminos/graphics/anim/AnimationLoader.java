package tk.luminos.graphics.anim;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import tk.luminos.filesystem.xml.XMLNode;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

public class AnimationLoader {
	
	private static final Matrix4 CORRECTION = new Matrix4();

	static {
		Matrix4.rotate((float) Math.toRadians(-90), new Vector3(1, 0, 0), new Matrix4(), CORRECTION);
	}
	
	private XMLNode animationData;
	private XMLNode jointHierarchy;
	
	public AnimationLoader(XMLNode animationData, XMLNode jointHierarchy){
		this.animationData = animationData;
		this.jointHierarchy = jointHierarchy;
	}
	
	public AnimationData extractAnimation(){
		String rootNode = findRootJointName();
		float[] times = getKeyTimes();
		float duration = times[times.length-1];
		KeyFrameData[] keyFrames = initKeyFrames(times);
		List<XMLNode> animationNodes = animationData.getChildren("animation");
		for(XMLNode jointNode : animationNodes){
			loadJointTransforms(keyFrames, jointNode, rootNode);
		}
		return new AnimationData(duration, keyFrames);
	}
	
	private float[] getKeyTimes(){
		XMLNode timeData = animationData.getChild("animation").getChild("source").getChild("float_array");
		String[] rawTimes = timeData.getData().split(" ");
		float[] times = new float[rawTimes.length];
		for(int i=0;i<times.length;i++){
			times[i] = Float.parseFloat(rawTimes[i]);
		}
		return times;
	}
	
	private KeyFrameData[] initKeyFrames(float[] times){
		KeyFrameData[] frames = new KeyFrameData[times.length];
		for(int i=0;i<frames.length;i++){
			frames[i] = new KeyFrameData(times[i]);
		}
		return frames;
	}
	
	private void loadJointTransforms(KeyFrameData[] frames, XMLNode jointData, String rootNodeId){
		String jointNameId = getJointName(jointData);
		String dataId = getDataId(jointData);
		XMLNode transformData = jointData.getChildWithAttribute("source", "id", dataId);
		String[] rawData = transformData.getChild("float_array").getData().split(" ");
		processTransforms(jointNameId, rawData, frames, jointNameId.equals(rootNodeId));
	}
	
	private String getDataId(XMLNode jointData){
		XMLNode node = jointData.getChild("sampler").getChildWithAttribute("input", "semantic", "OUTPUT");
		return node.getAttribute("source").substring(1);
	}
	
	private String getJointName(XMLNode jointData){
		XMLNode channelNode = jointData.getChild("channel");
		String data = channelNode.getAttribute("target");
		return data.split("/")[0];
	}
	
	private void processTransforms(String jointName, String[] rawData, KeyFrameData[] keyFrames, boolean root){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		float[] matrixData = new float[16];
		for(int i=0;i<keyFrames.length;i++){
			for(int j=0;j<16;j++){
				matrixData[j] = Float.parseFloat(rawData[i*16 + j]);
			}
			buffer.clear();
			buffer.put(matrixData);
			buffer.flip();
			Matrix4 transform = new Matrix4();
			transform.load(buffer);
			Matrix4 placeholder = new Matrix4();
			transform.transpose(placeholder);
			transform = placeholder;
			if(root){
				//because up axis in Blender is different to up axis in game
				Matrix4.mul(CORRECTION, transform, transform);
			}
			keyFrames[i].addJointTransform(new JointTransformData(jointName, transform));
		}
	}
	
	private String findRootJointName(){
		XMLNode skeleton = jointHierarchy.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		return skeleton.getChild("node").getAttribute("id");
	}

}
