package tk.luminos.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * 
 * Mouse Position Listener
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class MousePosition extends GLFWCursorPosCallback {

	private static double x, y;
	
	/**
	 * Constructor
	 */
	public MousePosition() {
		MousePosition.x = y = 0;
	}
	
	/**
	 * Gets x position of mouse
	 * 
	 * @return	X position
	 */
	public static double getX() {
		return x;
	}
	
	/**
	 * Gets y position of mouse
	 * 
	 * @return	Y Position
	 */
	public static double getY() {
		return y;
	}
	
	/**
	 * GLFW Invoke Method
	 */
	public void invoke(long window, double xpos, double ypos) {
		MousePosition.x = xpos;
		MousePosition.y = ypos;
	}

}
