package tk.luminos.physics;

/**
 * Stores intersection data
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class IntersectData {
	
	private boolean intersect;
	private float distance;
	
	/**
	 * Constructor
	 * 
	 * @param intersect		If is intersecting
	 * @param distance		Distance between objects
	 */
	public IntersectData(boolean intersect, float distance) {
		this.intersect = intersect;
		this.distance = distance;
	}
	
	/**
	 * Returns if objects are intersecting
	 * 
	 * @return		Intersection detected
	 */
	public boolean isIntersect() {
		return intersect;
	}
	
	/**
	 * Returns the distance between the objects
	 * 
	 * @return		Distance between objects
	 */
	public float getDistance() {
		return distance;
	}

}
