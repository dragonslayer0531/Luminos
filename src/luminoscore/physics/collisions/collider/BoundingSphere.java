package luminoscore.physics.collisions.collider;

import luminoscore.util.math.vector.Vector3f;

public class BoundingSphere {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/19/2016
	 */
	
	//Constructor fields
	private Vector3f center;
	private float radius;
	
	/*
	 * Default constructor
	 */
	public BoundingSphere() {
		
	}
	
	/*
	 * @param center Defines center of bounding sphere
	 * @param radius Defines the radius of the bounding sphere
	 * 
	 * Constructor
	 */
	public BoundingSphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Vector3f getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}

}
