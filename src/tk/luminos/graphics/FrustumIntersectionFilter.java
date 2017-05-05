package tk.luminos.graphics;

import tk.luminos.maths.Frustum;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;
import tk.luminos.physics.BoundingSphere;

/**
 * Allows for frustum culling
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class FrustumIntersectionFilter {
	
	private Frustum intersect;
	
	/**
	 * Creates new frustum intersection filter
	 */
	public FrustumIntersectionFilter() {
		intersect = new Frustum();
	}
	
	/**
	 * Updates frustum intersection
	 * 
	 * @param proj		projection matrix
	 * @param view		view matrix
	 */
	public void update(Matrix4 proj, Matrix4 view) {
		Matrix4 pv = Matrix4.mul(proj, view, null);
		intersect.set(pv);
	}
	
	/**
	 * Checks if the position is inside the viewing frustum
	 * 
	 * @param pos		position to check
	 * @return			is inside
	 */
	public boolean inside(Vector3 pos) {
		return intersect.point(pos);
	}
	
	public boolean inside(Vector3 pos, float rad) {
		return intersect.boundingSphere(new BoundingSphere(pos, rad));
	}

}
