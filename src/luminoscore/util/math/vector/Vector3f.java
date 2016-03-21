package luminoscore.util.math.vector;

public class Vector3f {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */

	//Constructor Fields
	public float x, y, z;
	
	/*
	 * Default Constructor
	 */
	public Vector3f() {
		x = y = z = 0;
	}
	
	/*
	 * @param x Defines X
	 * @param y Defines Y
	 * @param z Defines Z
	 * 
	 * Constructor
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//Calculates magnitude of the Vector
	public float magnitude() {
		float inside = x * x + y * y * z * z;
		return (float) Math.sqrt(inside);
	}
	
	//Calculates the normalised vector
	public Vector3f normalise() {
		float mag = this.magnitude();
		return new Vector3f(this.x / mag, this.y / mag, this.z / mag);
	}
	
	//Creates string equivalent of the object
	public String toString() {
		return new String("<Vector 3f>: " + x + " " + y +  " " + z);
	}
	
	/*
	 * @param left vector to be added to
	 * @param right vector to be added
	 * @return Vector3f
	 * 
	 * Adds two Vector3f
	 */
	public static Vector3f add(Vector3f left, Vector3f right) {
		return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
	}
	
	/*
	 * @param left vector to be subtracted from
	 * @param right vector to be subtracted
	 * @return Vector3f
	 * 
	 * Subtracts two Vector3f
	 */
	public static Vector3f sub(Vector3f left, Vector3f right) {
		return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
	}
	
	/*
	 * @param vec3 vector to be scaled
	 * @param scale how much to scale the vector by
	 * @return Vector3f
	 * 
	 * Scales a vector
	 */
	public static Vector3f scale(Vector3f vec3, float scale) {
		return new Vector3f(vec3.x * scale, vec3.y * scale, vec3.z * scale);
	}

}
