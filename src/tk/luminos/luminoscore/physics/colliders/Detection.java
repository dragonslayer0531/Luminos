package tk.luminos.luminoscore.physics.colliders;

import tk.luminos.luminoscore.graphics.gameobjects.Entity;
import tk.luminos.luminoscore.tools.Maths;

/**
 * Static methods for collision detection between two entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Detection {
	
	/**
	 * Detects overlaps in two {@link AABB}s
	 * 
	 * @param one		First entity
	 * @param two		Second entity
	 * @return			Evaluation of overlap
	 */
	public static boolean aabbToAABB(Entity one, Entity two) {
		Collider a1 = one.getCollider();
		Collider a2 = one.getCollider();
		if(a1 == null || a2 == null) return false;
		assert(a1 instanceof AABB && a2 instanceof AABB);
		AABB oneAABB = (AABB) a1;
		AABB twoAABB = (AABB) a2;
		if(Maths.isBetween(oneAABB.getYMinimum(), twoAABB.getYMinimum(), twoAABB.getYMaximum()) || Maths.isBetween(oneAABB.getYMaximum(), twoAABB.getYMinimum(), twoAABB.getYMaximum())) {
			if(Maths.isBetween(oneAABB.getYMinimum(), twoAABB.getYMinimum(), twoAABB.getYMaximum()) || Maths.isBetween(oneAABB.getYMaximum(), twoAABB.getYMinimum(), twoAABB.getYMaximum())) {
				if(Maths.isBetween(oneAABB.getZMinimum(), twoAABB.getZMinimum(), twoAABB.getZMaximum()) || Maths.isBetween(oneAABB.getZMaximum(), twoAABB.getZMinimum(), twoAABB.getZMaximum())) return true;
			}
		}
		return false;
	}
	
	/**
	 * Detects overlaps in two {@link BoundingSphere}s
	 * 
	 * @param one		First entity
	 * @param two		Second entity
	 * @return			Evaluation of overlap
	 */
	public static boolean boundingSphereToBoundingSphere(Entity one, Entity two) {
		Collider c1 = one.getCollider();
		Collider c2 = two.getCollider();
		if(c1 == null || c2 == null) return false;
		assert(c1 instanceof BoundingSphere && c2 instanceof BoundingSphere);
		BoundingSphere bsOne = (BoundingSphere) c1;
		BoundingSphere bsTwo = (BoundingSphere) c2;
		float distance = Maths.getDistance(bsOne.getCenter(), bsTwo.getCenter());
		return distance < (bsOne.getRadius() + bsTwo.getRadius());
	}

}
