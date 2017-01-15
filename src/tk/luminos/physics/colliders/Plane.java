package tk.luminos.physics.colliders;

import tk.luminos.tools.maths.vector.Vector3f;

public class Plane {
	
	private Vector3f normal;
	private float distance;
	
	public Plane(Vector3f normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}
	
	public Plane normalize() {
		float mag = normal.magnitude();
		return new Plane(new Vector3f(normal.x / mag, normal.y / mag, normal.z / mag), distance);
	}
	
	public IntersectData intersectBoundingSphere(BoundingSphere sphere) {
		float distanceFromSphereCenter = Vector3f.dot(normal, sphere.getLocation()) + distance;
		distanceFromSphereCenter = Math.abs(distanceFromSphereCenter);
		float distanceFromSphere = distanceFromSphereCenter - sphere.getRadius();
		return new IntersectData(distanceFromSphere < 0, distanceFromSphere);
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public float getDistance() {
		return distance;
	}

}
