package tk.luminos.physics;

import tk.luminos.maths.Vector3;

/**
 * Creates Axis-Aligned Bounding Box
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class AABB {
	
	private Vector3 minExtents;
	private Vector3 maxExtents;
	
	/**
	 * Constructor
	 * 
	 * @param minExtents	Minimum extents of AABB
	 * @param maxExtents	Maximum extends of AABB
	 */
	public AABB(Vector3 minExtents, Vector3 maxExtents) {
		this.minExtents = minExtents;
		this.maxExtents = maxExtents;
	}
	
	/**
	 * Gets minimum extents of AABB
	 * 
	 * @return	Minimum extents
	 */
	public Vector3 getMinExtents() {
		return minExtents;
	}
	
	/**
	 * Gets maximum extents of AABB
	 * 
	 * @return	Maximum extents
	 */
	public Vector3 getMaxExtents() {
		return maxExtents;
	}
	
	/**
	 * Checks if AABB is colliding
	 * 
	 * @param box		AABB to check for collision with
	 * @return			Intersection data
	 */
	public IntersectData intersect(AABB box) {
		Vector3 distances1 = Vector3.sub(box.minExtents, this.maxExtents, null);
		Vector3 distances2 = Vector3.sub(this.minExtents, box.maxExtents, null);
		Vector3 distance = Vector3.max(distances1, distances2);
		float maxDistance = distance.maxComponent();
		return new IntersectData(maxDistance < 0, maxDistance);
	}

}
