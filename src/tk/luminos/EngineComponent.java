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
	
	private Engine engine;
	
	/**
	 * Attaches the component to the engine
	 * 
	 * @param engine		Engine to attach this to
	 */
	public void attachTo(Engine engine) {
		this.engine = engine;
		engine.attach(this);
	}
	
	/**
	 * Detaches the component from the relevant engine
	 */
	public void detach() {
		engine.remove(this);
	}
	
	/**
	 * Updates the current component state
	 * 
	 * @param delta		Factor to update by
	 */
	public abstract void update(float delta);
	
	/**
	 * Used to close component and native resources
	 * held by the engine component
	 */
	public abstract void close();

}
