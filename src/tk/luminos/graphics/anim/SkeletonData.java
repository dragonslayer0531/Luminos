package tk.luminos.graphics.anim;

public class SkeletonData {
	
	public final int jointCount;
	public final JointData head;
	
	public SkeletonData(int jointCount, JointData head) {
		this.jointCount = jointCount;
		this.head = head;
	}

}
