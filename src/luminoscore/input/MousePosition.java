package luminoscore.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosition  extends GLFWCursorPosCallback {
	
	//Data fields
	private static double x, y;
	
	/*
	 * Constructor
	 * Creates the mouse position listener.  It is created when the window is created.
	 */
	public MousePosition() {
		MousePosition.x = y = 0;
	}
	
	//Returns X position of mouse cursor
	public static double getX() {
		return x;
	}
	
	//Returns Y position of mouse cursor
	public static double getY() {
		return y;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lwjgl.glfw.GLFWCursorPosCallback#invoke(long, double, double)
	 */
	public void invoke(long window, double xpos, double ypos) {
		MousePosition.x = xpos;
		MousePosition.y = ypos;
	}

}
