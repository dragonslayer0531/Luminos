package tk.luminos.graphics.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI manager
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class UI {
	
	protected static List<GUIObject> objects = new ArrayList<GUIObject>();
	
	/**
	 * Polls all objects in the manager
	 */
	public static void poll() {
		for (GUIObject uiElement : objects) {
			uiElement.poll();
		}
	}

}
