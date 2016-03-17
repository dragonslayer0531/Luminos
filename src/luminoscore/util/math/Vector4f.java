package luminoscore.util.math;

public class Vector4f {

	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */

	//Constructor Fields
	public float x, y, z, w;
	
	//Constructor
	public Vector4f() {
		x = y = z = w = 0;
	}
	
	//Constructor
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	//Calculates magnitude of the Vector
	public float magnitude() {
		float inside = x * x + y * y * z * z + w * w;
		return (float) Math.sqrt(inside);
	}
	
	//Calculates the normalised vector
	public Vector4f normalise() {
		float mag = this.magnitude();
		return new Vector4f(this.x / mag, this.y / mag, this.z / mag, this.w / mag);
	}
	
	//Creates string equivalent of the object
	public String toString() {
		return new String("<Vector 3f>: " + x + " " + y +  " " + z + " " + w);
	}
	
	/*
	 * @param left vector to be added to
	 * @param right vector to be added
	 * @return Vector4f
	 * 
	 * Adds two Vector4f
	 */
	public static Vector4f add(Vector4f left, Vector4f right) {
		return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
	}
	
	/*
	 * @param left vector to be subtracted from
	 * @param right vector to be subtracted
	 * @return Vector4f
	 * 
	 * Subtracts two Vector4f
	 */
	public static Vector4f sub(Vector4f left, Vector4f right) {
		return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
	}
	
	/*
	 * @param vec4 vector to be scaled
	 * @param scale how much to scale the vector by
	 * @return Vector4f
	 * 
	 * Scales a vector
	 */
	public static Vector4f scale(Vector4f vec4, float scale) {
		return new Vector4f(vec4.x * scale, vec4.y * scale, vec4.z * scale, vec4.w * scale);
	}

}
