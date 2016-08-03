package tk.luminos.maths.vector;

public class Vector4f implements Vector {

	public float x, y, z, w;

	public Vector4f() {
		this.x = this.y = this.z = this.w = 0;
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector4f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = vec.w;
	}

	public void normalise() {
		float l = magnitude();

		this.x /= l;
		this.y /= l;
		this.z /= l;
		this.w /= l;
	}

	public void add(Vector4f addend) {
		this.x += addend.x;
		this.y += addend.y;
		this.z += addend.z;
		this.w += addend.w;
	}

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

	public void sub(Vector4f subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
		this.z -= subtrahend.z;
		this.w -= subtrahend.w;
	}

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

	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		this.w *= scale;
	}

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

	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w)));
	}

	public static float dot(Vector4f left, Vector4f right) {
		return (left.x * right.x) + (left.y * right.y) + (left.z * right.z) + (left.w * right.w);
	}
	
	public String toString() {
		return "Vector4f [" + x + "," + y + "," + z + "," + w + "]";
	}

}
