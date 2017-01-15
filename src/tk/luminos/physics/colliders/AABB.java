package tk.luminos.physics.colliders;

import tk.luminos.tools.maths.vector.Vector3f;

public class AABB {
	
	private Vector3f minExtents;
	private Vector3f maxExtents;
	
	public AABB(Vector3f minExtents, Vector3f maxExtents) {
		this.minExtents = minExtents;
		this.maxExtents = maxExtents;
	}
	
	public Vector3f getMinExtents() {
		return minExtents;
	}
	public Vector3f getMaxExtents() {
		return maxExtents;
	}
	
	public IntersectData intersectAABB(AABB box) {
		Vector3f distances1 = Vector3f.sub(box.minExtents, this.maxExtents, null);
		Vector3f distances2 = Vector3f.sub(this.minExtents, box.maxExtents, null);
		Vector3f distance = Vector3f.max(distances1, distances2);
		float maxDistance = distance.maxComponent();
		return new IntersectData(maxDistance < 0, maxDistance);
	}

}
