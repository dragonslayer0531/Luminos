package luminoscore.graphics.entities.components;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Class of component that can be added to entity.  Used as a super class for custom components.
 * 
 */

public class Component {
	
	private Object o;

	/**
	 * @param o		Object to be wrapped into a component
	 * 
	 * Constructor that wraps object
	 */
	public Component(Object o) {
		this.o = o;
	}
	
	/**
	 * @return Object	Object wrapped by component
	 * 
	 * Gets the object wrapped by the component
	 */
	public Object getComponent() {
		return o;
	}

}
