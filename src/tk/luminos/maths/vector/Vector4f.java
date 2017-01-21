package tk.luminos.maths.vector;

/**
 * 
 * Creates 3 float array
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class Vector4f implements Vector {

	/**
	 * Entry variables
	 */
	public float x, y, z, w;

	/**
	 * Constructor initializing vector values to zero
	 */
	public Vector4f() {
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
	public Vector4f(float x, float y, float z, float w) {
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
	public Vector4f(Vector4f vec) {
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
	public void add(Vector4f addend) {
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
	public static Vector4f add(Vector4f left, Vector4f right, Vector4f dest) {
		if(dest == null) {
			return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
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
	public void sub(Vector4f subtrahend) {
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
	public static Vector4f sub(Vector4f left, Vector4f right, Vector4f dest) {
		if(dest == null) {
			return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
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
	public static Vector4f scale(Vector4f src, float scale, Vector4f dest) {
		if(dest == null) {
			return new Vector4f(src.x * scale, src.y * scale, src.z * scale, src.w * scale);
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
	public static float dot(Vector4f left, Vector4f right) {
		return (left.x * right.x) + (left.y * right.y) + (left.z * right.z) + (left.w * right.w);
	}
	
	/**
	 * Converts vector to string
	 */
	public String toString() {
		return "Vector4f [" + x + "," + y + "," + z + "," + w + "]";
	}

}
