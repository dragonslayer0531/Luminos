package tk.luminos.graphics.anim;

import tk.luminos.maths.Matrix4;

public class JointTransformData {
	
	public final String jointName;
	public final Matrix4 jointLocalTransform;
	
	public JointTransformData(String jointName, Matrix4 jointLocalTransform) {
		this.jointName = jointName;
		this.jointLocalTransform = jointLocalTransform;
	}

}
