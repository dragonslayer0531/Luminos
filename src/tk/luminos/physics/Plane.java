package tk.luminos.physics;

import tk.luminos.maths.vector.Vector3f;

/**
 * Creates plane for intersection
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Plane {
	
	private Vector3f normal;
	private float distance;
	
	/**
	 * Constructor
	 * 
	 * @param normal		Normal of plane
	 * @param distance		Distance from origin
	 */
	public Plane(Vector3f normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}
	
	/**
	 * Normalizes plane
	 * 
	 * @return	Normalized Plane
	 */
	public Plane normalize() {
		float mag = new Vector3f(normal.x, normal.y, normal.z).magnitude();
		return new Plane(new Vector3f(normal.x / mag, normal.y / mag, normal.z / mag), distance);
	}
	
	/**
	 * Checks for collision with sphere
	 * 
	 * @param sphere	Other collider
	 * @return			Intersection data
	 */
	public IntersectData intersect(BoundingSphere sphere) {
		float distanceFromSphereCenter = Vector3f.dot(normal, sphere.getLocation()) + distance;
		distanceFromSphereCenter = Math.abs(distanceFromSphereCenter);
		float distanceFromSphere = distanceFromSphereCenter - sphere.getRadius();
		return new IntersectData(distanceFromSphere < 0, distanceFromSphere);
	}
	
	/**
	 * Gets normal of plane
	 * 
	 * @return		Plane normal
	 */
	public Vector3f getNormal() {
		return normal;
	}
	
	/**
	 * Gets distance of plane from origin
	 * 	
	 * @return	Distance from origin
	 */
	public float getDistance() {
		return distance;
	}

}
