package tk.luminos.gameobjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a new component based entity
 * 
 * @author Nick Clark 
 * @version 1.0
 */
public abstract class ComponentEntity implements Renderable {
	
	protected Map<String, Component<?>> components = new HashMap<String, Component<?>>();
	
	/**
	 * Get the component corresponding to the given name
	 * 
	 * @param name		name of component
	 * @return			value of component
	 */
	public final Component<?> getComponent(String name)
	{
		return components.get(name);
	}
	
	/**
	 * Adds a component to the entity
	 * 
	 * @param name		Name of component
	 * @param value		Value of component
	 */
	public final void addComponent(String name, Component<?> value)
	{
		components.put(name, value);
	}
	
	/**
	 * Removes the component corresponding to the given name
	 * 
	 * @param name		name of component
	 * @return			value of component
	 */
	public final Component<?> remove(String name)
	{
		return components.remove(name);
	}

}
