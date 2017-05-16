package tk.luminos.gameobjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a new component based entity
 * 
 * @author Nick Clark 
 * @version 1.0
 */
abstract class ComponentEntity {
	
	protected Map<Class<?>, Component<?>> components = new HashMap<Class<?>, Component<?>>();
	
	/**
	 * Get the component corresponding to the given name
	 * 
	 * @param name		class of component
	 * @return			value of component
	 */
	public Component<?> getComponent(Class<?> name)
	{
		return components.get(name);
	}
	
	/**
	 * Adds a component to the entity
	 * 
	 * @param value		Value of component
	 */
	public void addComponent(Component<?> value)
	{
		components.put(value.getClass(), value);
	}
	
	/**
	 * Removes the component corresponding to the given name
	 * 
	 * @param name		name of component
	 * @return			value of component
	 */
	public Component<?> remove(String name)
	{
		return components.remove(name);
	}

}
