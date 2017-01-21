package tk.luminos.maths.vector;

import tk.luminos.maths.MathUtils;

/**
 * 
 * Creates 3 float array
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Vector3f implements Vector {
	
	/**
	 * Entry variables
	 */
	public float x, y, z;
	
	/**
	 * Constructor initializing vector values to zero
	 */
	public Vector3f() {
		this.x = this.y = this.z = 0;
	}
	
	/**
	 * Creates new vector
	 * 
	 * @param x		X position
	 * @param y		Y position
	 * @param z		Z position
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates new vector from vector
	 * 
	 * @param vec		Old vector
	 */
	public Vector3f(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	/**
	 * Add vector to this vector
	 * 
	 * @param addend		Added vector
	 */
	public void add(Vector3f addend) {
		this.x += addend.x;
		this.y += addend.y;
		this.z += addend.z;
	}

	/**
	 * Adds two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @param dest		Destination vector
	 * @return			If destination is null
	 * 						create a new vector
	 * 						Then return sum vector
	 */
	public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
		if(dest == null) {
			return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
		} else {
			dest.x = left.x + right.x;
			dest.y = left.y + right.y;
			dest.z = left.z + right.z;
			return null;
		}
	}
	
	/**
	 * Subtract vector from this vector
	 * 
	 * @param subtrahend	Subtracted vector
	 */
	public void sub(Vector3f subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
		this.z -= subtrahend.z;
	}
	
	/**
	 * Subtracts two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @param dest		Destination vector
	 * @return			If destination is null
	 * 						create a new vector
	 * 						Then return difference vector
	 */
	public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
		if(dest == null) {
			return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
		} else {
			dest.x = left.x - right.x;
			dest.y = left.y - right.y;
			dest.z = left.z - right.z;
			return null;
		}
	}
	
	/**
	 * Scales vector by float value
	 * 
	 * @param scale		Scale factor
	 */
	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}
	
	/**
	 * Scales vector by float value
	 * 
	 * @param src		Source vector
	 * @param scale		Scale factor
	 * @param dest		Destination vector
	 * @return			If destination is null
	 * 						create new vector
	 * 						Then return scaled vector
	 */
	public static Vector3f scale(Vector3f src, float scale, Vector3f dest) {
		if(dest == null) {
			return new Vector3f(src.x * scale, src.y * scale, src.z * scale);
		} else {
			dest.x = src.x * scale;
			dest.y = src.y * scale;
			dest.z = src.z * scale;
			return null;
		}
	}
	
	/**
	 * Calculates magnitude of vector
	 * 
	 * @return 		magnitude of vector
	 */
	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z)));
	}
	
	/**
	 * Calculates dot product of two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @return			Dot product
	 */
	public static float dot(Vector3f left, Vector3f right) {
		return (left.x * right.x) + (left.y * right.y) + (left.z * right.z);
	}
	
	/**
	 * Creates cross product of two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @param dest		Destination vector
	 * @return			If destination is null
	 * 						create new vector
	 * 						Then return cross product 
	 */
	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null)
			dest = new Vector3f();

		dest.x = left.y * right.z - left.z * right.y;
		dest.y = right.x * left.z - right.z * left.x;
		dest.z = left.x * right.y - left.y * right.x;

		return dest;
	}
	
	/**
	 * Creates a negating vector
	 * 
	 * @return		negating vector
	 */
	public Vector3f negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/**
	 * Normalizes vector
	 */
	public void normalize() {
		float l = magnitude();

		this.x /= l;
		this.y /= l;
		this.z /= l;
	}
	
	public static Vector3f max(Vector3f one, Vector3f two) {
		float x, y, z;
		if (one.x > two.x)
			x = one.x;
		else
			x = two.x;
		if (one.y > two.y)
			y = one.y;
		else
			y = two.y;
		if (one.z > two.z)
			z = one.z;
		else 
			z = two.z;
		return new Vector3f(x, y, z);
	}
	
	public float maxComponent() {
		return MathUtils.getMaximum(new float[] {x, y, z});
	}
	
	/**
	 * Converts vector to string
	 */
	public String toString() {
		return "Vector3f [" + x + "," + y + "," + z + "]";
	}


}
