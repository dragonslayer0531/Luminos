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
