package tk.luminos.graphics.anim;

import java.util.Map;

public class KeyFrame {
	
	private final float timeStamp;
	private final Map<String, JointTransform> pose;
	
	public KeyFrame(float timeStamp, Map<String, JointTransform> jointKeyFrames) {
		this.timeStamp = timeStamp;
		this.pose = jointKeyFrames;
	}

	public float getTimeStamp() {
		return timeStamp;
	}

	public Map<String, JointTransform> getJointKeyFrames() {
		return pose;
	}

}
