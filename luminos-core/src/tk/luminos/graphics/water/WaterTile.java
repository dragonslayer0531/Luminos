package tk.luminos.graphics.water;

import tk.luminos.maths.Vector2;

/**
 * 
 * Holds data for the water tile
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class WaterTile {
	
	private float height;
	private float x,z;
	private Vector2 scale;
	
	/**
	 * Constructor
	 * 
	 * @param centerX	Center of tile, X
	 * @param centerZ	Center of tile, Z
	 * @param height	Y Value of tile
	 * @param scale		Scale of tile
	 */
	public WaterTile(float centerX, float centerZ, float height, Vector2 scale) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.scale = scale;
	}
	
	private float scale_float;
	
	/**
	 * Constructor
	 * 
	 * @param position	Center of tile
	 * @param height	Height of tile
	 * @param scale		Scale of tile
	 */
	public WaterTile(Vector2 position, float height, float scale) {
		this.x = position.x;
		this.z = position.y;
		this.height = height;
		this.scale_float = scale;
	}

	/**
	 * Gets height of tile
	 * 
	 * @return	Height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Gets X coordinate
	 * 
	 * @return 	X
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets Z coordinate
	 * 
	 * @return	Z
	 */
	public float getZ() {
		return z;
	}
	
	/**
	 * Gets scale
	 * 
	 * @return	scale
	 */
	public Vector2 getScale() {
		return scale;
	}
	
	/**
	 * Gets scale
	 * 
	 * @return	scale
	 */
	public float getFloatScale() {
		return scale_float;
	}

}
