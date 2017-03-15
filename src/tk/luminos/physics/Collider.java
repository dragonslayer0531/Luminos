package tk.luminos.physics;

/**
 * Collider abstract class
 * 
 * @author Nick Clark
 * @version 1.0
 */
public abstract class Collider {
	
	/**
	 * Checks for intersection
	 * 
	 * @return If intersecting
	 */
	public abstract boolean isColliding();
	
	/**
	 * Response of collider
	 * 
	 * @param delta		Factor of response
	 */
	public abstract void response(float delta);

}
