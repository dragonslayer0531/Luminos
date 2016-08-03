package tk.luminos.maths.vector;

public class Vector3f implements Vector {
	
public float x, y, z;
	
	public Vector3f() {
		this.x = this.y = this.z = 0;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public void add(Vector3f addend) {
		this.x += addend.x;
		this.y += addend.y;
		this.z += addend.z;
	}

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
	
	public void sub(Vector3f subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
		this.z -= subtrahend.z;
	}
	
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
	
	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}
	
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
	
	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z)));
	}
	
	public static float dot(Vector3f left, Vector3f right) {
		return (left.x * right.x) + (left.y * right.y) + (left.z * right.z);
	}
	
	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null)
			dest = new Vector3f();

		dest.x = left.y * right.z - left.z * right.y;
		dest.y = right.x * left.z - right.z * left.x;
		dest.z = left.x * right.y - left.y * right.x;

		return dest;
	}
	
	public Vector3f negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public void normalise() {
		float l = magnitude();

		this.x /= l;
		this.y /= l;
		this.z /= l;
	}
	
	public String toString() {
		return "Vector3f [" + x + "," + y + "," + z + "]";
	}


}
