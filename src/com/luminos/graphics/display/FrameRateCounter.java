package com.luminos.graphics.display;

import org.lwjgl.glfw.GLFW;

import com.luminos.LuminosException;

/**
 * 
 * The public class keeping track of FPS
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class FrameRateCounter {
	
	private double start;
	private double end;
	private double frameTime = 0;
	private short frames = 0;
	private double fps;

	/**
	 * Constructor of the frame rate counter
	 */
	public FrameRateCounter() {
		
	}
	
	/**
	 * Starts the frame rate counter.  Called at beginning of each GLFW update
	 * 
	 * @throws LuminosException		Checks if GLFW has been initialized
	 */
	public void start() throws LuminosException {
		if(GLFW.glfwInit()) {
			start = GLFW.glfwGetTime();
		} else {
			throw new LuminosException("GLFW NOT INITIALISED");
		}
	}
	
	/**
	 * Ends the frame time counter and calculates.  Called at end of each GLFW update
	 * 
	 * @throws LuminosException		Checks if GLFW has been initialized
	 */
	public void calculate() throws LuminosException {
		if(GLFW.glfwInit()) {
			end = GLFW.glfwGetTime();
		} else {
			throw new LuminosException("GLFW NOT INITIALISED");
		}
		frameTime = frameTime + end - start;
		++frames;
		if(frameTime >= (1)) {
			fps = frames;
			frames = 0;
			frameTime = 0;
		}
		
	}
	
	/**
	 * Gets the frames per second count
	 * 
	 * @return Value of FPS 
	 */
	public int getFPS() {
		return (int) fps;
	}
	
	/**
	 * Gets the length of time per frame
	 * 
	 * @return Value of seconds per frame
	 */
	public float getFrameTime() {
		return 1 / (float) GLFWWindow.REFRESH_RATE;
	}
	
}
