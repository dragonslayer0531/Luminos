package tk.luminos.graphics.ui;

import tk.luminos.graphics.Texture;
import tk.luminos.maths.Vector2;

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
	private Vector2 position;
	private Vector2 scale;
	
	/**
	 * Constructor
	 * 
	 * @param texture	GPU texture ID
	 * @param position	Position of GUI
	 * @param scale		Scale of texture
	 */
	public GUITexture(int texture, Vector2 position, Vector2 scale) {
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
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the texture
	 * 
	 * @param position	Updated position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * Gets scale of GUI Texture
	 * 
	 * @return Vector2f	Scale of GUI Texture 
	 */
	public Vector2 getScale() {
		return scale;
	}

}
