package tk.luminos.luminoscore.graphics.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import tk.luminos.luminoscore.graphics.textures.GuiTexture;
import tk.luminos.luminoscore.input.MousePosition;

/**
 * Creates a GUI Button Interface
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUIButton implements GUIObject {
	
	private GuiTexture inactive;
	private GuiTexture active;
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
		this.inactive = new GuiTexture(inactiveTexture, position, scale);
		this.active = new GuiTexture(activeTexture, position, scale);
	}
	
	/**
	 * Gets if/where the button is clicked

	 * @return 			Mouse location
	 */
	public Vector2f getClickLocation() {
		Vector2f mouse_loc = new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
		poll(mouse_loc);
		if(!isActive) return null;
		return new Vector2f(mouse_loc);
	}
	
	public boolean isDown() {
		Vector2f mouse_loc = new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
		poll(mouse_loc);
		return isActive;
	}

	/**
	 * Gets the active {@link GuiTexture}
	 * 
	 * @return	active texture	
	 */
	public List<GuiTexture> getTextures() {
		List<GuiTexture> tex = new ArrayList<GuiTexture>();
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
