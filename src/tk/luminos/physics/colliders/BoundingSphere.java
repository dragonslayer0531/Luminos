package tk.luminos.physics.colliders;

import tk.luminos.tools.maths.vector.Vector3f;

public class BoundingSphere {
	
	private Vector3f location;
	private float radius;
	
	public BoundingSphere(Vector3f location, float radius) {
		this.location = location;
		this.radius = radius;
	}
	
	public Vector3f getLocation() {
		return location;
	}
	public void setLocation(Vector3f location) {
		this.location = location;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public IntersectData intersectBoundingSphere(BoundingSphere sphere) {
		float radiusDistance = this.radius + sphere.radius;
		float centerDistance = Vector3f.sub(sphere.location, this.location, null).magnitude();
		return new IntersectData(centerDistance < radiusDistance, centerDistance - radiusDistance);
	}

}
