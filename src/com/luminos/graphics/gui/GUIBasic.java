package com.luminos.graphics.gui;

import java.util.List;

import com.luminos.graphics.input.MousePosition;
import com.luminos.graphics.textures.GUITexture;
import com.luminos.maths.vector.Vector2f;

/**
 * Basic GUI Image system
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUIBasic implements GUIObject {
	
	private List<GUITexture> guiTextures;
	
	/**
	 * Constructor
	 * 
	 * @param textureID		Texture ID of GUI Object
	 * @param location		Location of GUI	Object
	 * @param size			Size of GUI Object
	 */
	public GUIBasic(int textureID, Vector2f location, Vector2f size) {
		guiTextures.add(new GUITexture(textureID, location, size));
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
	 * Gets {@link GUITexture}s used
	 * 
	 * @return 	textures used
	 */
	public List<GUITexture> getTextures() {
		return guiTextures;
	}

}
