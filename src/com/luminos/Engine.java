package com.luminos;

import com.luminos.graphics.display.GLFWWindow;
import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.Entity;
import com.luminos.graphics.loaders.Loader;
import com.luminos.graphics.render.MasterRenderer;
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
	 * Updates physics
	 */
	public void update() {
		
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
	public void render(GameLogic logic, Entity entity, Camera camera, GLFWWindow window) throws InterruptedException {
		this.update();
		logic.input(window, entity, camera);
		logic.render(manager, camera);
		sync();
		window.update();
	}
	
	private void sync() throws InterruptedException {
		float loopSlot = 1f / ConfigData.FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			Thread.sleep(1);
		}
	}

}
