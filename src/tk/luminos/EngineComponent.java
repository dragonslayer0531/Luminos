package tk.luminos;

/**
 * 
 * Abstract class for creating engine components to 
 * attach to main graphics engine thread
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public abstract class EngineComponent extends Thread {
	
	/**
	 * Updates the current component state
	 * 
	 * @param scene		Scene to draw
	 */
	public abstract void update(Scene scene);

}
