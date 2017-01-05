package com.luminos;

import com.luminos.graphics.display.Window;
import com.luminos.graphics.render.MasterRenderer;
import com.luminos.loaders.Loader;
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
	private float elapsedTime;
	private float accumulator = 0f;
	private float interval = 1f / ConfigData.UPS;
	
	/**
	 * Creates a new Engine
	 * 
	 * @param masterRenderer	Wraps all required renderers
	 * @param loader			Loads the required objects to the GPU
	 * @throws Exception 
	 */
	public Engine(MasterRenderer masterRenderer, Loader loader) throws Exception {
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
		window.printDeviceData();
	}
	
	/**
	 * Renders GameLogic to the scene
	 * 
	 * @param logic						Logic to render
	 * @param entity					Entity controlled by user
	 * @param camera					Camera to render with
	 * @param window					Window to render to
	 * @throws Exception 
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
	
	public void close() {
		manager.dispose();
	}
	
	private void sync() throws InterruptedException {
		float loopSlot = 1f / Window.REFRESH_RATE;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			
		}
	}

}
