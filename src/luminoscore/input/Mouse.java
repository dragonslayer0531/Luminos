package luminoscore.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback {
	
	//Mouse buttons
	public static final int MOUSE_BUTTON_1 = 0x0, MOUSE_BUTTON_2 = 0x1,
			MOUSE_BUTTON_3 = 0x2, MOUSE_BUTTON_4 = 0x3, MOUSE_BUTTON_5 = 0x4,
			MOUSE_BUTTON_6 = 0x5, MOUSE_BUTTON_7 = 0x6, MOUSE_BUTTON_8 = 0x7,
			MOUSE_BUTTON_LAST = MOUSE_BUTTON_8,
			MOUSE_BUTTON_LEFT = MOUSE_BUTTON_1,
			MOUSE_BUTTON_RIGHT = MOUSE_BUTTON_2,
			MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_3;

	//Arrays containing the states of the mouse buttons
	private static int[] buttonState = new int[MOUSE_BUTTON_LAST];
	private static int[] buttonDown = new int[MOUSE_BUTTON_LAST];

	
	/*
	 * Creates the Mouse Listener.  It is created when the window is created.
	 */
	public Mouse() {
		for (int i = 0; i < buttonState.length; i++) {
			buttonState[i] = -1;
		}
	}

	//Updates the button arrays to show all buttons are not engaged
	public void update() {
		for (int i = 0; i < buttonState.length; i++) {
			buttonState[i] = -1;
		}
	}
	
	/*
	 * @param key uses an integer to represent the mouse button
	 * @return boolean if key is down
	 */
	public static boolean isDown(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonDown[key] == 1;
		}
		return false;
	}

	/*
	 * @param key uses an integer to represent the mouse button
	 * @return boolean if key is pressed
	 */
	public static boolean isPressed(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonState[key] == 1;
		}
		return false;
	}
	
	/*
	 * @param key uses an integer to represent the mouse button
	 * @return boolean if key is released
	 */
	public static boolean isReleased(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonState[key] == 0;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lwjgl.glfw.GLFWMouseButtonCallback#invoke(long, int, int, int)
	 */
	public void invoke(long window, int button, int action, int mods) {
		if (button <= MOUSE_BUTTON_LAST) {
			buttonState[button] = action;
			buttonDown[button] = action != GLFW_RELEASE ? 1 : 0;
		}
	}

}