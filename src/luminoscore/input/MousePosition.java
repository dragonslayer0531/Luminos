package luminoscore.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Mouse Position Listener
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
	 * @return double	X position
	 * 
	 * Gets x position of mouse
	 */
	public static double getX() {
		return x;
	}
	
	/**
	 * @return double	Y Position
	 * 
	 * Gets y position of mouse
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
