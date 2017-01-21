package tk.luminos.physics.colliders;

import tk.luminos.maths.vector.Vector3f;

/**
 * Creates Axis-Aligned Bounding Box
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class AABB {
	
	private Vector3f minExtents;
	private Vector3f maxExtents;
	
	/**
	 * Constructor
	 * 
	 * @param minExtents	Minimum extents of AABB
	 * @param maxExtents	Maximum extends of AABB
	 */
	public AABB(Vector3f minExtents, Vector3f maxExtents) {
		this.minExtents = minExtents;
		this.maxExtents = maxExtents;
	}
	
	/**
	 * Gets minimum extents of AABB
	 * 
	 * @return	Minimum extents
	 */
	public Vector3f getMinExtents() {
		return minExtents;
	}
	
	/**
	 * Gets maximum extents of AABB
	 * 
	 * @return	Maximum extents
	 */
	public Vector3f getMaxExtents() {
		return maxExtents;
	}
	
	/**
	 * Checks if AABB is colliding
	 * 
	 * @param box		AABB to check for collision with
	 * @return			Intersection data
	 */
	public IntersectData intersect(AABB box) {
		Vector3f distances1 = Vector3f.sub(box.minExtents, this.maxExtents, null);
		Vector3f distances2 = Vector3f.sub(this.minExtents, box.maxExtents, null);
		Vector3f distance = Vector3f.max(distances1, distances2);
		float maxDistance = distance.maxComponent();
		return new IntersectData(maxDistance < 0, maxDistance);
	}

}
