package tk.luminos;

import org.lwjgl.system.Callback;

import tk.luminos.display.Window;
import tk.luminos.graphics.RenderEngine;
import tk.luminos.graphics.SceneManager;
import tk.luminos.graphics.shaders.GLSLVersion;
import tk.luminos.loaders.Loader;
import tk.luminos.physics.PhysicsEngine;
import tk.luminos.util.Timer;

/**
 * 
 * Manages core processes of the Luminos Engine
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class Engine {
	
	private static Timer timer = new Timer();
	private static Callback glErrorCallback;	
	private static RenderEngine renderEngine;
	private static PhysicsEngine physicsEngine;
	private static Window window;
	
	public static RenderMode mode = RenderMode.NUKLEAR_OPENGL;
	
	/**
	 * Current version of GLSL used by the engine.
	 */
	public static final GLSLVersion GLSL_VERSION = GLSLVersion.VERSION330;
	
	/**
	 * Current error stream of luminos engine
	 */
	public static final Stream ERROR_STREAM = new Stream();

	/**
	 * Creates a new Engine
	 * 
	 * @throws Exception 		Thrown if shader programs do not compile properly
	 */
	public static void createEngine() throws Exception {
		renderEngine = new RenderEngine(new SceneManager());
		Thread.currentThread().setName("LUMINOS_ENGINE:_GRAPHICS");
	}
	
	/**
	 * Creates a new Engine
	 * 
	 * @param glslVersion		Version of GLSL to use in shader programs
	 * @throws Exception 		Thrown if shader programs do not compile properly
	 */
	public static void createEngine(GLSLVersion glslVersion) throws Exception {
		renderEngine = new RenderEngine(new SceneManager());
		Thread.currentThread().setName("LUMINOS_ENGINE:_GRAPHICS");
	}
	
	/**
	 * Opens window
	 * 
	 * @param window		Window to open
	 */
	public static void start(Window window) {
		Engine.window = window;
		Engine.window.showWindow();
		glErrorCallback = DebugUtil.setupDebugMessageCallback((source, type, id, severity, message) -> {
			if (!severity.equals("NOTIFICATION"))
				System.err.println(severity + "\n" + source + "\n" + type + "\n" + message);
		});
		if (physicsEngine != null) {
			if(System.getProperty("os.name").contains("mac")) {
				physicsEngine.run();
			}
			else {
				physicsEngine.start();
			}
		}
	}
	
	/**
	 * Renders GameLogic to the scene
	 * 
	 * @param scene						Logic to render
	 * @param window					Window to render to
	 * @throws Exception 				Thrown if shader program cannot be created or other code fails
	 */
	public static void update(Scene scene, Window window) throws Exception {
		scene.input(window);
		renderEngine.update(scene);
		physicsEngine.update(scene);
		window.update();
		if (window.isVsync())
			sync();
	}
	
	/**
	 * Closes engine
	 * 
	 * @throws Exception	thrown if error in joining threads
	 */
	public static void close() throws Exception {
		if (physicsEngine != null) {
			physicsEngine.join();
		}
		renderEngine.dispose();
		renderEngine.join();
		glErrorCallback.free();
		Loader.getInstance().dispose();
	}
	
	/**
	 * Adds physics engine to the engine context
	 * 
	 * @param engine	physics engine to add
	 */
	public static void addPhysicsEngine(PhysicsEngine engine) {
		Engine.physicsEngine = engine;
	}
	
	private static void sync() throws InterruptedException {
		float loopSlot = 1f / Window.REFRESH_RATE;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			
		}
	}

}
