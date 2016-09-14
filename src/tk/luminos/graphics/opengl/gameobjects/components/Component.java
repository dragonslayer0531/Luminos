package tk.luminos.graphics.opengl.gameobjects.components;

/**
 * 
 * Class of component that can be added to entity.  Used as a super class for custom components.
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class Component {
	
	private Object o;

	/**
	 * Constructor that wraps object
	 * 
	 * @param o		Object to be wrapped into a component
	 */
	public Component(Object o) {
		this.o = o;
	}
	
	/**
	 * Gets the object wrapped by the component
	 * 
	 * @return Object	Object wrapped by component 
	 */
	public Object getComponent() {
		return o;
	}

}
