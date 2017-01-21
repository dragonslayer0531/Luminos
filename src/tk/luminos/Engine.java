package tk.luminos;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.Callback;
import org.lwjgl.system.Configuration;

import tk.luminos.graphics.display.Window;
import tk.luminos.graphics.render.MasterRenderer;
import tk.luminos.graphics.shaders.GLSLVersion;
import tk.luminos.loaders.Loader;
import tk.luminos.utilities.SceneManager;
import tk.luminos.utilities.Timer;

/**
 * 
 * Manages core processes of the Luminos Engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Engine {
	
	private SceneManager manager;
	private Timer timer = new Timer();
	private float elapsedTime;
	private float accumulator = 0f;
	private float interval = 1f / ConfigData.UPS;
	private Callback glErrorCallback;
	
	private List<EngineComponent> components = new ArrayList<EngineComponent>();
	
	/**
	 * Current version of GLSL used by the engine.
	 */
	public static final GLSLVersion GLSL_VERSION = GLSLVersion.VERSION330;
	
	/**
	 * Creates a new Engine
	 * 
	 * @param masterRenderer	Wraps all required renderers
	 * @param loader			Loads the required objects to the GPU
	 * @throws Exception 		Thrown if shader programs do not compile properly
	 */
	public Engine(MasterRenderer masterRenderer, Loader loader) throws Exception {
		this.manager = new SceneManager(masterRenderer, loader);
		Thread.currentThread().setName("LUMINOS_ENGINE:_GRAPHICS");
	}
	
	/**
	 * Creates a new Engine
	 * 
	 * @param masterRenderer	Wraps all required renderers
	 * @param loader			Loads the required objects to the GPU
	 * @param glslVersion		Version of GLSL to use in shader programs
	 * @throws Exception 		Thrown if shader programs do not compile properly
	 */
	public Engine(MasterRenderer masterRenderer, Loader loader, GLSLVersion glslVersion) throws Exception {
		this.manager = new SceneManager(masterRenderer, loader);
		Thread.currentThread().setName("LUMINOS_ENGINE:_GRAPHICS");
	}
	
	/**
	 * Opens window
	 * 
	 * @param window		Window to open
	 */
	public void start(Window window) {
		window.showWindow();
		glErrorCallback = DebugUtil.setupDebugMessageCallback((source, type, id, severity, message) -> {
			if (severity.equalsIgnoreCase("HIGH")) {
				System.err.println(severity + "\n" + source + "\n" + type + "\n" + message);
			}	
		});
		Configuration.DEBUG.set(true);
	}
	
	/**
	 * Renders GameLogic to the scene
	 * 
	 * @param logic						Logic to render
	 * @param window					Window to render to
	 * @throws Exception 				Thrown if shader program cannot be created or other code fails
	 */
	public void render(Scene logic, Window window) throws Exception {
		elapsedTime = timer.getElapsedTime();
		accumulator += elapsedTime;
		logic.input(window, logic.getFocalObject(), logic.getCamera());
		while (accumulator >= interval) {
			logic.update(interval);
			accumulator -= interval;
		}
		logic.render(manager, logic.getCamera());
		window.update();
		if (window.isVsync())
			sync();
	}
	
	/**
	 * Attaches {@link EngineComponent} to the engine
	 * 	
	 * @param component		Component to add
	 */
	public void attach(EngineComponent component) {
		this.components.add(component);
	}
	
	/**
	 * Removes {@link EngineComponent} from the engine
	 * 	
	 * @param component		Component to remove
	 */
	public void remove(EngineComponent component) {
		this.components.remove(component);
	}
	
	/**
	 * Closes engine
	 */
	public void close() {
		for (EngineComponent component : components) {
			component.close();
		}
		components.clear();
		glErrorCallback.free();
		manager.dispose();
	}
	
	private void sync() throws InterruptedException {
		float loopSlot = 1f / Window.REFRESH_RATE;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			
		}
	}

}
