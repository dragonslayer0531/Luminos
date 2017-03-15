package tk.luminos.graphics;

import java.util.List;

/**
 * Interface used by all GUI Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface GUIObject {
	
	/**
	 * Gets all {@link GUITexture}s used by the GUIObject
	 * 
	 * @return	all textures
	 */
	public List<GUITexture> getTextures();
	
}
