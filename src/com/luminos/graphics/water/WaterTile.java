package com.luminos.graphics.water;

import com.luminos.tools.maths.vector.Vector2f;

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
	private Vector2f scale;
	
	/**
	 * Constructor
	 * 
	 * @param centerX	Center of tile, X
	 * @param centerZ	Center of tile, Z
	 * @param height	Y Value of tile
	 * @param scale		Scale of tile
	 */
	public WaterTile(float centerX, float centerZ, float height, Vector2f scale) {
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
	public WaterTile(Vector2f position, float height, float scale) {
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
	public Vector2f getScale() {
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
