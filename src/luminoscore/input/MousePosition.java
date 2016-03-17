package luminoscore.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePosition  extends GLFWCursorPosCallback {

	private static double x, y;
	
	public MousePosition() {
		MousePosition.x = y = 0;
	}
	
	public static double getX() {
		return x;
	}
	
	public static double getY() {
		return y;
	}
	
	public void invoke(long window, double xpos, double ypos) {
		MousePosition.x = xpos;
		MousePosition.y = ypos;
	}

}
