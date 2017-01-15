package tk.luminos.physics.colliders;

public interface Collider {
	
	public boolean checkIntersection();
	public boolean isColliding();
	public void response(float interval);

}
