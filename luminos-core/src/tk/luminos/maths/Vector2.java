package tk.luminos.maths;

import tk.luminos.serialization.DBArray;
import tk.luminos.serialization.Serializable;

/**
 * 
 * Creates 2 float array
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Vector2 implements Serializable<DBArray>, Vector {
	
	/**
	 * Entry variables
	 */
	public float x, y;
	
	public final static int SIZE = 2;
	
	/**
	 * Constructor initializing vector values to zero
	 */
	public Vector2() {
		this.x = this.y = 0;
	}
	
	/**
	 * Creates new vector
	 * 
	 * @param x		X position
	 * @param y		Y position
	 */
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates new vector from vector
	 * 
	 * @param vec		Old vector
	 */
	public Vector2(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	/**
	 * Add vector to this vector
	 * 
	 * @param addend		Added vector
	 */
	public void add(Vector2 addend) {
		this.x += addend.x;
		this.y += addend.y;
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
	public static Vector2 add(Vector2 left, Vector2 right, Vector2 dest) {
		if(dest == null) {
			return new Vector2(left.x + right.x, left.y + right.y);
		} else {
			dest.x = left.x + right.x;
			dest.y = left.y + right.y;
			return null;
		}
	}
	
	/**
	 * Subtract vector from this vector
	 * 
	 * @param subtrahend	Subtracted vector
	 */
	public void sub(Vector2 subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
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
	public static Vector2 sub(Vector2 left, Vector2 right, Vector2 dest) {
		if(dest == null) {
			return new Vector2(left.x - right.x, left.y - right.y);
		} else {
			dest.x = left.x - right.x;
			dest.y = left.y - right.y;
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
	public static Vector2 scale(Vector2 src, float scale, Vector2 dest) {
		if(dest == null) {
			return new Vector2(src.x * scale, src.y * scale);
		} else {
			dest.x = src.x * scale;
			dest.y = src.y * scale;
			return null;
		}
	}
	
	/**
	 * Calculates magnitude of vector
	 * 
	 * @return 		magnitude of vector
	 */
	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y)));
	}
	
	/**
	 * Calculates dot product of two vectors
	 * 
	 * @param left		Left vector
	 * @param right		Right vector
	 * @return			Dot product
	 */
	public static float dot(Vector2 left, Vector2 right) {
		return (left.x * right.x) + (left.y * right.y);
	}
	
	/**
	 * Normalizes vector
	 */
	public void normalize() {
		float l = magnitude();
		
		this.x /= l;
		this.y /= l;
	}
	
	/**
	 * Converts vector to string
	 */
	public String toString() {
		return "Vector2f [" + x + "," + y + "]";
	}
	
	public float dot(Vector y) {
		if (!(y instanceof Vector2))
			throw new IllegalArgumentException("Can only dot Vector2 with Vector2");
		Vector2 other = (Vector2) y;
		return this.x * other.x + this.y * other.y;
	}
	
	/**
	 * Serializes a Vector2 object
	 * 
	 * @return data base array of object
	 */
	public DBArray serialize(String name) {
		return DBArray.createFloatArray(name, new float[] {x, y});
	}


}
