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
	
	protected Type obj;
	/**
	 * Constructor that wraps object
	 * 
	 * @param obj		Object to be wrapped into a component
	 */
	public Component(Type obj) {
		this.obj = obj;
	}
	
	/**
	 * Gets the object wrapped by the component
	 * 
	 * @return Object	Object wrapped by component 
	 */
	public Type getComponent() {
		return obj;
	}

}
