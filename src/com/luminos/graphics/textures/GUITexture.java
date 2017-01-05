package com.luminos.graphics.textures;

import com.luminos.tools.maths.vector.Vector2f;

/**
 * 
 * GUI Texture
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUITexture implements Texture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	/**
	 * Constructor
	 * 
	 * @param texture	GPU texture ID
	 * @param position	Position of GUI
	 * @param scale		Scale of texture
	 */
	public GUITexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * Gets texture ID of GUI Texture
	 * @return  GPU Texture ID
	 */
	public int getID() {
		return texture;
	}

	/**
	 * Gets position of GUI Texture
	 * 
	 * @return Position of GUI Texture 
	 */
	public Vector2f getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the texture
	 * 
	 * @param position	Updated position
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * Gets scale of GUI Texture
	 * 
	 * @return Vector2f	Scale of GUI Texture 
	 */
	public Vector2f getScale() {
		return scale;
	}

}
