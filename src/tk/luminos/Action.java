package tk.luminos;

/**
 * 
 * Abstract class for setting up actions in the engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class Action {
	
	/**
	 * Determines if a given action was performed
	 * 
	 * @return		Was action performed
	 */
	public abstract boolean actionPerformed();
	
	/**
	 * Executes the action to take if the action was
	 * performed
	 */
	public abstract void act();

}
