package tk.luminos.luminoscore.graphics.gui;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import tk.luminos.luminoscore.graphics.textures.GuiTexture;

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
	public List<GuiTexture> getTextures();
	
}
