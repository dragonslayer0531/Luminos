package luminoscore.graphics.water;

import org.lwjgl.util.vector.Vector2f;

public class WaterTile {
	
	private float height;
	private float x,z;
	private Vector2f scale;
	
	public WaterTile(float centerX, float centerZ, float height, Vector2f scale) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.scale = scale;
	}
	
	private Vector2f position;
	private float scale_float;
	
	public WaterTile(Vector2f position, float height, float scale) {
		this.position = position;
		this.height = height;
		this.scale_float = scale;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public float getFloatScale() {
		return scale_float;
	}
	
	public Vector2f getPosition() {
		return position;
	}

}
