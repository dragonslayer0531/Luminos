package tk.luminos.physics.colliders;

public class IntersectData {
	
	private boolean intersect;
	private float distance;
	
	public IntersectData(boolean intersect, float distance) {
		this.intersect = intersect;
		this.distance = distance;
	}
	
	public boolean isIntersect() {
		return intersect;
	}
	public float getDistance() {
		return distance;
	}
	
	//TODO:  Implement collision location

}
