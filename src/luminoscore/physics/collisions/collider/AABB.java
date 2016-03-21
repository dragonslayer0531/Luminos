package luminoscore.physics.collisions.collider;

import luminoscore.util.math.vector.Vector3f;

public class AABB {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/19/2016
	 */
	
	//Constructor fields
	private Vector3f cornerOne, cornerTwo;
	
	/*
	 * Default Constructor
	 */
	public AABB() {
		cornerOne = cornerTwo = new Vector3f();
	}

	/*
	 * @param cornerOne Defines one corner of the AABB
	 * @param cornerTwo Defines the opposite corner of the AABB
	 * 
	 * Constructor
	 */
	public AABB(Vector3f cornerOne, Vector3f cornerTwo) {
		this.cornerOne = cornerOne;
		this.cornerTwo = cornerTwo;
	}

	//Getter Methods
	public Vector3f getCornerOne() {
		return cornerOne;
	}

	public Vector3f getCornerTwo() {
		return cornerTwo;
	}

}
