package tk.luminos.graphics.gui;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.input.MousePosition;
import tk.luminos.maths.vector.Vector2f;

/**
 * Creates a GUI Button Interface
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUIButton implements GUIObject {
	
	private GUITexture inactive;
	private GUITexture active;
	private boolean isActive = false;

	/**
	 * Constructor
	 * 
	 * @param inactiveTexture		Texture for when button is inactive
	 * @param activeTexture			Texture for when button is active
	 * @param position				Position to place button
	 * @param scale					Scale of button
	 */
	public GUIButton(int inactiveTexture, int activeTexture, Vector2f position, Vector2f scale) {
		this.inactive = new GUITexture(inactiveTexture, position, scale);
		this.active = new GUITexture(activeTexture, position, scale);
	}
	
	/**
	 * Gets if/where the button is clicked

	 * @return 			Mouse location
	 */
	public Vector2f getClickLocation() {
		Vector2f mouse_loc = new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
		poll(mouse_loc);
		if(!isActive) return null;
		return new Vector2f(mouse_loc.x, mouse_loc.y);
	}
	
	public boolean isDown() {
		Vector2f mouse_loc = new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
		poll(mouse_loc);
		return isActive;
	}

	/**
	 * Gets the active {@link GUITexture}
	 * 
	 * @return	active texture	
	 */
	public List<GUITexture> getTextures() {
		List<GUITexture> tex = new ArrayList<GUITexture>();
		if(isActive) 
			tex.add(active);
		else tex.add(inactive);
		return tex;
	}

//******************************************Private Methods*****************************************//	
	
	/**
	 * Polls the button for activity
	 * 
	 * @param mouse		Location of mouse
	 */
	private void poll(Vector2f mouse) {
		Vector2f center = inactive.getPosition();
		Vector2f scale = inactive.getScale();
		if(center.x + scale.x < mouse.x && center.x - scale.x > mouse.x) {
			if(center.y + scale.y < mouse.y && center.y - scale.y > mouse.y) isActive = true;
		} else {
			isActive = false;
		}
	}

}
