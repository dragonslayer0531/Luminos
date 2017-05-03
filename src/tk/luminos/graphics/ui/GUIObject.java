package tk.luminos.graphics.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface used by all GUI Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class GUIObject {
	
	private boolean hover, pressed;
	protected List<GUITexture> textures;
	
	/**
	 * Creates new GUI Object
	 * 
	 * @param textures		textures to add
	 */
	public GUIObject(GUITexture... textures) {
		this.textures = new ArrayList<GUITexture>();
		for (GUITexture texture : textures)
			this.textures.add(texture);
	}
	
	/**
	 * Default active method
	 * 
	 * @return	current texture
	 */
	public GUITexture active() {
		return textures.get(0);
	}
	
	/**
	 * Polls objects for changes
	 */
	public abstract void poll();
	
	/**
	 * Gets if object is being hovered over
	 * 
	 * @return	if hovered over
	 */
	public boolean hover() {
		return hover;
	}
	
	/**
	 * Gets if object is pressed
	 * 
	 * @return	if pressed
	 */
	public boolean pressed() {
		return pressed;
	}
	
}
