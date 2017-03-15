package tk.luminos.physics;

import tk.luminos.maths.vector.Vector3f;

/**
 * Creates bounding sphere
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class BoundingSphere {
	
	private Vector3f location;
	private float radius;
	
	/**
	 * Constructor
	 * 
	 * @param location		Center
	 * @param radius		Size
	 */
	public BoundingSphere(Vector3f location, float radius) {
		this.location = location;
		this.radius = radius;
	}
	
	/**
	 * Gets center of bounding sphere
	 * 
	 * @return	Center of sphere
	 */
	public Vector3f getLocation() {
		return location;
	}
	
	/**
	 * Sets the center of bounding sphere
	 * 
	 * @param location		New center
	 */
	public void setLocation(Vector3f location) {
		this.location = location;
	}
	
	/**
	 * Gets the radius of the bounding sphere
	 * 
	 * @return		Radius
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Sets the radius of the bounding sphere
	 * 
	 * @param radius New radius 
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	/**
	 * Tests if the spheres are colliding
	 * 
	 * @param sphere	Other sphere
	 * @return			Intersection data
	 */
	public IntersectData intersect(BoundingSphere sphere) {
		float radiusDistance = this.radius + sphere.radius;
		float centerDistance = Vector3f.sub(sphere.location, this.location, null).magnitude();
		return new IntersectData(centerDistance < radiusDistance, centerDistance - radiusDistance);
	}

}
