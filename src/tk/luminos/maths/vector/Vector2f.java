package tk.luminos.maths.vector;

public class Vector2f implements Vector {
	
	public float x, y;
	
	public Vector2f() {
		this.x = this.y = 0;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public void add(Vector2f addend) {
		this.x += addend.x;
		this.y += addend.y;
	}

	public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(left.x + right.x, left.y + right.y);
		} else {
			dest.x = left.x + right.x;
			dest.y = left.y + right.y;
			return null;
		}
	}
	
	public void sub(Vector2f subtrahend) {
		this.x -= subtrahend.x;
		this.y -= subtrahend.y;
	}
	
	public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(left.x - right.x, left.y - right.y);
		} else {
			dest.x = left.x - right.x;
			dest.y = left.y - right.y;
			return null;
		}
	}
	
	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
	}
	
	public static Vector2f scale(Vector2f src, float scale, Vector2f dest) {
		if(dest == null) {
			return new Vector2f(src.x * scale, src.y * scale);
		} else {
			dest.x = src.x * scale;
			dest.y = src.y * scale;
			return null;
		}
	}
	
	public float magnitude() {
		return (float) (Math.sqrt((this.x * this.x) + (this.y * this.y)));
	}
	
	public static float dot(Vector2f left, Vector2f right) {
		return (left.x * right.x) + (left.y * right.y);
	}
	
	public void normalise() {
		float l = magnitude();
		
		this.x /= l;
		this.y /= l;
	}
	
	public String toString() {
		return "Vector2f [" + x + "," + y + "]";
	}


}
