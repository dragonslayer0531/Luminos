package tk.luminos.maths.vector;

/**
 * 
 * Creates 2 float array
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Vector2f implements Vector {
	
	/**
	 * Entry variables
	 */
	public float x, y;
	
	/**
	 * Constructor initializing vector values to zero
	 */
	public Vector2f() {
		this.x = this.y = 0;
	}
	
	/**
	 * Creates new vector
	 * 
	 * @param x		X position
	 * @param y		Y position
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates new vector from vector
	 * 
	 * @param vec		Old vector
	 */
	public Vector2f(Vector2f vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	/**
	 * Add vector to this vector
	 * 
	 * @param addend		Added vector
	 */
	public void add(Vector2f addend) {
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
	public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(left.x + right.x, left.y + right.y);
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
	public void sub(Vector2f subtrahend) {
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
	public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(left.x - right.x, left.y - right.y);
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
	public static Vector2f scale(Vector2f src, float scale, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(src.x * scale, src.y * scale);
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
	public static float dot(Vector2f left, Vector2f right) {
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


}
