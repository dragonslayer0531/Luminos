package tk.luminos.graphics.anim;

public class AnimatedModelData {
	
	private final SkeletonData joints;
	private final MeshData mesh;
	
	public AnimatedModelData(MeshData mesh, SkeletonData joints) {
		this.joints = joints;
		this.mesh = mesh;
	}

	public SkeletonData getJoints() {
		return joints;
	}

	public MeshData getMesh() {
		return mesh;
	}

}
