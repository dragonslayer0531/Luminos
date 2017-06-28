package tk.luminos.maths;

import tk.luminos.serialization.DBArray;
import tk.luminos.serialization.Serializable;

/**
 * 
 * Creates 3 float array
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class Vector4 implements Serializable<DBArray>, Vector {

	/**
	 * Entry variables
	 */
	public float x, y, z, w;
	
	public final static int SIZE = 4;

	/**
	 * Constructor initializing vector values to zero
	 */
	public Vector4() {
		this.x = this.y = this.z = this.w = 0;
	}

	/**
	 * Creates new vector
	 * 
	 * @param x		X position
	 * @param y		Y position
	 * @param z		Z position
	 * @param w		W position
	 */
	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates new vector from vector
	 * 
	 * @param vec		Old vector
	 */
	public Vector4(Vector4 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = vec.w;
	}

	/**
	 * Normalizes vector
	 */
	public void normalize() {
		float l = magnitude();

		this.x /= l;
		this.y /= l;
		this.z /= l;
		this.w /= l;
	}

	/**
	 * Add vector to this vector
	 * 
	 * @param addend		Added vector
	 */
	public void add(Vector4 addend) {
		this.x += addend.x;
		this.y += addend.y;
		this.z += addend.z;
		this.w += addend.w;
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
	public static Vector4 add(Vector4 left, Vector4 right, Vector4 dest) {
		if(dest == null) {
			return new Vector4(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
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
	public void sub(Vector4 subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
		this.z -= subtrahend.z;
		this.w -= subtrahend.w;
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
	public static Vector4 sub(Vector4 left, Vector4 right, Vector4 dest) {
		if(dest == null) {
			return new Vector4(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
		} else {
			dest.x = left.x - right.x;
			dest.y = left.y - right.y;
			dest.z = left.z - right.z;
			dest.w = left.w - right.w;
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
		this.w *= scale;
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
	public static Vector4 scale(Vector4 src, float scale, Vector4 dest) {
		if(dest == null) {
			return new Vector4(src.x * scale, src.y * scale, src.z * scale, src.w * scale);
		} else {
			dest.x = src.x * scale;
			dest.y = src.y * scale;
			dest.z = src.z * scale;
			dest.w = src.w * scale;
			return null;
		}
	}

	/**
	 * Calculates magnitude of vector
	 * 
	 * @return 		magnitude of vector
	 */
	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w)));
	}

	/**
	 * Calculates dot product of two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @return			Dot product
	 */
	public static float dot(Vector4 left, Vector4 right) {
		return (left.x * right.x) + (left.y * right.y) + (left.z * right.z) + (left.w * right.w);
	}
	
	public float dot(Vector y) {
		if (!(y instanceof Vector4))
			throw new IllegalArgumentException("Can only dot Vector4 with Vector4");
		Vector4 other = (Vector4) y;
		return this.x * other.x + this.y * other.y + this.z * other.z + this.w + other.w;
	}
	
	/**
	 * Converts vector to string
	 */
	public String toString() {
		return "Vector4f [" + x + "," + y + "," + z + "," + w + "]";
	}
	
	/**
	 * Serializes a Vector4 object
	 * 
	 * @return data base array of object
	 */
	public DBArray serialize(String name) {
		return DBArray.createFloatArray(name, new float[] {x, y, z, w});
	}

}
