package com.luminos;

import org.lwjgl.glfw.GLFW;

import com.luminos.graphics.display.GLFWWindow;
import com.luminos.graphics.loaders.Loader;
import com.luminos.graphics.render.MasterRenderer;
import com.luminos.input.Keyboard;
import com.luminos.tools.SceneManager;
import com.luminos.tools.Timer;

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
	
	/**
	 * Creates a new Engine
	 * 
	 * @param masterRenderer	Wraps all required renderers
	 * @param loader			Loads the required objects to the GPU
	 */
	public Engine(MasterRenderer masterRenderer, Loader loader) {
		this.manager = new SceneManager(masterRenderer, loader);
	}
	
	/**
	 * Opens window
	 * 
	 * @param windowID		Window to open
	 */
	public void start(long windowID) {
		GLFW.glfwShowWindow(windowID);
		Thread.currentThread().setName("ENGINE");
	}
	
	/**
	 * Updates physics
	 */
	public void update(GameLogic logic) {
		logic.update();
	}
	
	/**
	 * Renders GameLogic to the scene
	 * 
	 * @param logic						Logic to render
	 * @param entity					Entity controlled by user
	 * @param camera					Camera to render with
	 * @param window					Window to render to
	 * @throws InterruptedException		Thrown when synchronizing the thread causes an error
	 */
	public void render(GameLogic logic, GLFWWindow window) throws InterruptedException {
		while (!window.shouldClose()) {
			if (closeRequest())
				break;
			logic.input(window, logic.getFocalObject(), logic.getCamera());
			logic.update();
			logic.render(manager, logic.getCamera());
			window.update();
			sync();
		}
	}
	
	private boolean closeRequest() {
		return Keyboard.isDown(Keyboard.KEY_ESCAPE);
	}
	
	private void sync() throws InterruptedException {
		float loopSlot = 1f / ConfigData.FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			Thread.sleep(1);
		}
	}

}
