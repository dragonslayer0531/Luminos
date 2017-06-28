package tk.luminos.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * 
 * Mouse Listener
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Mouse {

	/** Mouse buttons. See <a target="_blank" href="http://www.glfw.org/docs/latest/input.html#input_mouse_button">mouse button input</a> for how these are used. */
	public static final int
			MOUSE_BUTTON_1      = 0,
			MOUSE_BUTTON_2      = 1,
			MOUSE_BUTTON_3      = 2,
			MOUSE_BUTTON_4      = 3,
			MOUSE_BUTTON_5      = 4,
			MOUSE_BUTTON_6      = 5,
			MOUSE_BUTTON_7      = 6,
			MOUSE_BUTTON_8      = 7,
			MOUSE_BUTTON_LAST   = MOUSE_BUTTON_8,
			MOUSE_BUTTON_LEFT   = MOUSE_BUTTON_1,
			MOUSE_BUTTON_RIGHT  = MOUSE_BUTTON_2,
			MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_3;
	
	private boolean[] buttons_down;
	private boolean[] buttons_released;
	
	private double x, y;
	private double prevX, prevY;
	
	private static Mouse instance;
	
	static {
		instance = new Mouse();
	}
	
	private Mouse() {
		buttons_down = new boolean[32];
		buttons_released = new boolean[32];
	}
	
	public static Mouse getInstance() {
		return instance;
	}
	
	public void update_position(double x, double y) {
		this.prevX = this.x;
		this.prevY = this.y;
		this.x = x;
		this.y = y;
	}
	
	public void update_buttons(int button, int action) {
		buttons_down[button] = action == GLFW_PRESS;
		buttons_released[button] =  action == GLFW_RELEASE;
	}
	
	public boolean isDown(int button) {
		return buttons_down[button];
	}
	
	public boolean isReleased(int button) {
		return buttons_released[button];
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getDX() {
		return x - prevX;
	}
	
	public double getDY() {
		return y - prevY;
	}

}
