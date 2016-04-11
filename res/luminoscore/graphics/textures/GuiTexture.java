package luminoscore.graphics.textures;

import org.lwjgl.util.vector.Vector2f;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * GUI Texture
 *
 */

public class GuiTexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	/**
	 * @param texture	GPU texture ID
	 * @param position	Position of GUI
	 * @param scale		Scale of Entity
	 * 
	 * Constructor
	 */
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * @return int GPU Texture ID
	 * 
	 * Gets texture ID of GUI Texture
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * @return Vector2f	Position of GUI Texture
	 * 
	 * Gets position of GUI Texture
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return Vector2f	Scale of GUI Texture
	 * 
	 * Gets scale of GUI Texture
	 */
	public Vector2f getScale() {
		return scale;
	}

}
