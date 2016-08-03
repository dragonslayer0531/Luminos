package tk.luminos.physics.colliders;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.gameobjects.Entity;
import tk.luminos.tools.Maths;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Creates a bounding sphere for a mesh
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class BoundingSphere implements Collider {
	
	private float radius;
	private Vector3f center;
	
	/**
	 * Null constructor
	 */
	public BoundingSphere() {
		
	}

	/**
	 * Generates Bounding Sphere
	 * 
	 * @param entity	Entity to generate Bounding Sphere
	 */
	public void generate(Entity entity) {
		AABB placeholder = new AABB();
		placeholder.generate(entity);
		float xCenter = (placeholder.getXMaximum() + placeholder.getXMinimum()) / 2;
		float yCenter = (placeholder.getYMaximum() + placeholder.getYMinimum()) / 2;
		float zCenter = (placeholder.getZMaximum() + placeholder.getZMinimum()) / 2;
		this.center = new Vector3f(xCenter, yCenter, zCenter);
		Vector3f test1 = new Vector3f(placeholder.getXMaximum(), placeholder.getYMaximum(), placeholder.getZMaximum());
		Vector3f test2 = new Vector3f(placeholder.getXMinimum(), placeholder.getYMinimum(), placeholder.getZMinimum());
		List<Vector3f> tests = new ArrayList<Vector3f>();
		tests.add(test1);
		tests.add(test2);
		Vector3f max = Maths.getFurthestPoint(center, tests);
		this.radius = Maths.getDistance(center, max);
	}

	/**
	 * Gets the radius of the bounding sphere
	 * 
	 * @return	radius of the bounding sphere
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Gets the center position of the bounding sphere
	 * 
	 * @return	radius of the bounding sphere
	 */
	public Vector3f getCenter() {
		return center;
	}

}
