package tk.luminos.luminoscore.graphics.gui;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import tk.luminos.luminoscore.graphics.textures.GuiTexture;
import tk.luminos.luminoscore.input.MousePosition;

/**
 * Basic GUI Image system
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUIBasic implements GUIObject {
	
	private List<GuiTexture> guiTextures;
	
	/**
	 * Constructor
	 * 
	 * @param textureID		Texture ID of GUI Object
	 * @param location		Location of GUI	Object
	 * @param size			Size of GUI Object
	 */
	public GUIBasic(int textureID, Vector2f location, Vector2f size) {
		guiTextures.add(new GuiTexture(textureID, location, size));
	}

	/**
	 * Gets the click location on the GUIObject
	 * 
	 * @return	click location on the GUIObject.  Returns null if not on object.
	 */
	public Vector2f getClickLocation() {
		return new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
	}

	/**
	 * Gets {@link GuiTexture}s used
	 * 
	 * @return 	textures used
	 */
	public List<GuiTexture> getTextures() {
		return guiTextures;
	}

}
