package tk.luminos.graphics.gui;

import java.util.List;

import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.maths.vector.Vector2f;

/**
 * Interface used by all GUI Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface GUIObject {
	
	/**
	 * Gets the click location on the GUIObject
	 * 
	 * @return	click location on the GUIObject.  Returns null if not on object.
	 */
	public Vector2f getClickLocation();
	
	/**
	 * Gets all {@link GuiTexture}s used by the GUIObject
	 * 
	 * @return	all textures
	 */
	public List<GUITexture> getTextures();
	
}
