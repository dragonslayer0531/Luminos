package tk.luminos.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Keyboard {
	
	private boolean[] keys_repeat;
	private boolean[] keys_pressed;
	private boolean[] keys_released;
	private boolean[] keys_down;
	
	private static Keyboard instance;
	
	static {
		instance = new Keyboard();
	}
	
	private Keyboard() {
		keys_repeat = new boolean[1024];
		keys_pressed = new boolean[1024];
		keys_released = new boolean[1024];
		keys_down = new boolean[1024];
	}
	
	public void update(int key, int action) {
		keys_pressed[key] = action == GLFW_PRESS;
		keys_repeat[key] = action == GLFW_REPEAT;
		keys_released[key] = action == GLFW_RELEASE;
		keys_down[key] = action != GLFW_RELEASE;
	}
	
	public static Keyboard getInstance() {
		return instance;
	}

	public boolean isKeyDown(int key) {
		return keys_down[key];
	}

}
