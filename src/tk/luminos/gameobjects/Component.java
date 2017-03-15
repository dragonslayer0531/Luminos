package tk.luminos.gameobjects;

/**
 * 
 * Class of component that can be added to entity.  Used as a super class for custom components.
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class Component<Type> {
	
	protected Object obj;
	/**
	 * Constructor that wraps object
	 * 
	 * @param o		Object to be wrapped into a component
	 */
	public Component(Object obj) {
		this.obj = obj;
	}
	
	/**
	 * Gets the object wrapped by the component
	 * 
	 * @return Object	Object wrapped by component 
	 */
	@SuppressWarnings("unchecked")
	public Type getComponent() {
		return (Type) obj;
	}

}
