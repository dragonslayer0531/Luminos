package com.luminos.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * 
 * Mouse Listener
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Mouse extends GLFWMouseButtonCallback {

	public static final int MOUSE_BUTTON_1 = 0x0, MOUSE_BUTTON_2 = 0x1,
			MOUSE_BUTTON_3 = 0x2, MOUSE_BUTTON_4 = 0x3, MOUSE_BUTTON_5 = 0x4,
			MOUSE_BUTTON_6 = 0x5, MOUSE_BUTTON_7 = 0x6, MOUSE_BUTTON_8 = 0x7,
			MOUSE_BUTTON_LAST = MOUSE_BUTTON_8,
			MOUSE_BUTTON_LEFT = MOUSE_BUTTON_1,
			MOUSE_BUTTON_RIGHT = MOUSE_BUTTON_2,
			MOUSE_BUTTON_MIDDLE = MOUSE_BUTTON_3;

	private static int[] buttonState = new int[MOUSE_BUTTON_LAST];
	private static int[] buttonDown = new int[MOUSE_BUTTON_LAST];

	/**
	 * Constructor
	 */
	public Mouse() {
		for (int i = 0; i < buttonState.length; i++) {
			buttonState[i] = -1;
		}
	}

	/**
	 * Updates state method
	 */
	public void update() {
		for (int i = 0; i < buttonState.length; i++) {
			buttonState[i] = -1;
		}
	}

	/**
	 * Gets if button is down	
	 * 
	 * @param key		KEY CODE
	 * @return	Is down
	 */
	public static boolean isDown(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonDown[key] == 1;
		}
		return false;
	}

	/**
	 * Gets if button is pressed
	 * 
	 * @param key		KEY CODE
	 * @return	Is pressed
	 */
	public static boolean isPressed(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonState[key] == 1;
		}
		return false;
	}

	/**
	 * Gets if button is released
	 * 
	 * @param key		KEY CODE
	 * @return	Is released
	 */
	public static boolean isReleased(int key) {
		if (key <= MOUSE_BUTTON_LAST) {
			return buttonState[key] == 0;
		}
		return false;
	}

	/**
	 * GLFW Invoke Method
	 */
	public void invoke(long window, int button, int action, int mods) {
		if (button <= MOUSE_BUTTON_LAST) {
			buttonState[button] = action;
			buttonDown[button] = action != GLFW_RELEASE ? 1 : 0;
		}
	}

}
