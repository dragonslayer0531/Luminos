package tk.luminos.loaders;

import static tk.luminos.graphics.anim.ColladaLoader.loadColladaAnimation;

import java.util.HashMap;
import java.util.Map;

import tk.luminos.graphics.anim.Animation;
import tk.luminos.graphics.anim.AnimationData;
import tk.luminos.graphics.anim.JointTransform;
import tk.luminos.graphics.anim.JointTransformData;
import tk.luminos.graphics.anim.KeyFrame;
import tk.luminos.graphics.anim.KeyFrameData;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Quaternion;
import tk.luminos.maths.Vector3;
import tk.luminos.utilities.File;

public class AnimationLoader {
	
	public static Animation loadAnimation(File file) throws Exception {
		AnimationData anim = loadColladaAnimation(file);
		KeyFrame[] frames = new KeyFrame[anim.keyFrames.length];
		for (int i = 0; i < frames.length; i++)
			frames[i] = createKeyFrame(anim.keyFrames[i]);
		return new Animation(anim.length, frames);
	}
	
	
	private static KeyFrame createKeyFrame(KeyFrameData kfd) {
		final Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jdi : kfd.jointTransforms) {
			JointTransform jd = createTransform(jdi);
			map.put(jdi.jointName,  jd);
		}
		return new KeyFrame(kfd.time, map);
	}
	
	private static JointTransform createTransform(JointTransformData jtd) {
		Matrix4 mat = jtd.jointLocalTransform;
		Vector3 translation = new Vector3(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}

}
