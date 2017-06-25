package tk.luminos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tk.luminos.display.Window;
import tk.luminos.util.Configuration;

/**
 * 
 * Class implemented for running the application
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Application {
	
	private List<Event> actions = new ArrayList<Event>();
	private List<Thread> threads = new ArrayList<Thread>();
	private Scene scene;
	
	private static Configuration config;
	
	/**
	 * Loads the settings file to all applications
	 * 
	 * @param file				name of file
	 * @throws IOException		thrown if file cannot be found/opened
	 */
	public static void loadSettings(String file) throws IOException {
		config = Configuration.loadSettings(file);
	}
	
	/**
	 * Sets configuration setting
	 * 
	 * @param setting		name of setting
	 * @param value			value of setting
	 */
	public static void setValue(String setting, Integer value)  {
		config.setValue(setting, value);
	}
	
	/**
	 * Gets integer value of configuration
	 * 
	 * @param setting		name of setting
	 * @return				value of setting
	 */
	public static Integer getValue(String setting) {
		return config.getValue(setting);
	}
	
	/**
	 * Represents whether or not the application should close.  It is
	 * set to false by default.
	 */
	public boolean shouldClose = false;
	
	/**
	 * Renders the current scene to the default frame buffer
	 * 
	 * @param window		Window to render to
	 * @throws Exception	Thrown if rendering failed
	 */
	public void render(Window window) throws Exception {
		while (!window.shouldClose() && !shouldClose) {
			for (Event action : actions) {
				if (action.eventPerformed())
					action.act();
			}
			scene.getGameObjects().stream().parallel().forEach(obj -> obj.update());
			Engine.update(scene, window);
		}
		this.shouldClose = true;
	}
	
	/**
	 * Adds {@link Event} to the application
	 * 
	 * @param action		Action to add
	 */
	public void addEvent(Event action) {
		this.actions.add(action);
	}
	
	/**
	 * Swaps current rendering scene
	 * 
	 * @param scene		Scene to render
	 * @return			Previous scene
	 */
	public Scene setActiveScene(Scene scene) {
		Scene old = this.scene;
		this.scene = scene;
		return old;
	}
	
	/**
	 * Attaches thread to application
	 * 
	 * @param thread		Thread to attach
	 */
	public void attachThread(Thread thread) {
		threads.add(thread);
		String os_name = System.getProperty("os.name");
		if(os_name.contains("mac")) {
			thread.run();
		}
		else {
			thread.start();
		}
	}
	
	/**
	 * Forces all threads to join
	 * 
	 * @throws Exception		Thrown if threads fail to join
	 */
	public void close() throws Exception {
		for (Thread thread : threads) {
			thread.join();
		}
	}
	
	/**
	 * Gets all threads attached to application
	 * 
	 * @return		Threads attached to application
	 */
	public List<Thread> getThreads() {
		return threads;
	}
	
	/**
	 * Gets the thread attached to application defined by some name
	 * 
	 * @param name		Name of thread to search for
	 * @return			Thread with given name
	 */
	public Thread getThreadByName(String name) {
		for (Thread thread : threads) 
			if (thread.getName().equals(name))
				return thread;
		return null;
	}

}
