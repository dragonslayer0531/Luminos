package tk.luminos.graphics;

/**
 * 
 * Interface for all objects in scene
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface SceneObject {
	
	/**
	 * Gets the ID of the object
	 * 
	 * @return	ID of object
	 */
	public default String getID() { return null; }
	
	/**
	 * Sets the ID of the object
	 * 
	 * @param id		ID of object
	 */
	public default void setID(String id) { return; }

}
