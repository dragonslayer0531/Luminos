package luminoscore.util.math.vector;

public class Vector2f {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */

	//Constructor Fields
	public float x, y;
	
	/*
	 * Default Constructor
	 */
	public Vector2f() {
		x = y = 0;
	}
	
	/*
	 * @param x Defines X
	 * @param y Defines Y
	 * 
	 * Constructor
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//Calculates magnitude of the Vector
	public float magnitude() {
		float inside = x * x + y * y;
		return (float) Math.sqrt(inside);
	}
	
	//Calculates the normalised vector
	public Vector2f normalise() {
		float mag = this.magnitude();
		return new Vector2f(this.x / mag, this.y / mag);
	}
	
	//Creates string equivalent of the object
	public String toString() {
		return new String("<Vector 3f>: " + x + " " + y);
	}
	
	/*
	 * @param left vector to be added to
	 * @param right vector to be added
	 * @return Vector2f
	 * 
	 * Adds two Vector2f
	 */
	public static Vector2f add(Vector2f left, Vector2f right) {
		return new Vector2f(left.x + right.x, left.y + right.y);
	}
	
	/*
	 * @param left vector to be subtracted from
	 * @param right vector to be subtracted
	 * @return Vector2f
	 * 
	 * Subtracts two Vector2f
	 */
	public static Vector2f sub(Vector2f left, Vector2f right) {
		return new Vector2f(left.x - right.x, left.y - right.y);
	}
	
	/*
	 * @param vec3 vector to be scaled
	 * @param scale how much to scale the vector by
	 * @return Vector2f
	 * 
	 * Scales a vector
	 */
	public static Vector2f scale(Vector2f vec3, float scale) {
		return new Vector2f(vec3.x * scale, vec3.y * scale);
	}

	
}