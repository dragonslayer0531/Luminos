package com.luminos.input;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

/**
 * 
 * XBOXController listener
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class XBOXController {
	
	public static final int
	GLFW_JOYSTICK_1    = 0x0;
	
	public static final int
	XBOX_A = 0x0,
	XBOX_B = 0x1,
	XBOX_X = 0x2,
	XBOX_Y = 0x3,
	XBOX_LB = 0x4,
	XBOX_RB = 0x5,
	XBOX_VIEW = 0x6,
	XBOX_MENU = 0x7,
	XBOX_LEFT_STICK = 0x8,
	XBOX_RIGHT_STICK = 0x9;
	
	/**
	 * Gets the horizontal movement of the controller
	 * 
	 * @return	Horizontal movement
	 */
	public static float getHorizontalAxis() {
		return glfwGetJoystickAxes(GLFW_JOYSTICK_1).get(0);
	}
	
	/**
	 * Gets the vertical movement of the controller
	 * 
	 * @return	Vertical movement
	 */
	public static float getVerticalAxis() {
		return glfwGetJoystickAxes(GLFW_JOYSTICK_1).get(1);
	}
	
	/**
	 * Gets the horizontal look axis
	 * 
	 * @return	Horizontal look movement
	 */
	public static float getHorizontalAxisLook() {
		return glfwGetJoystickAxes(GLFW_JOYSTICK_1).get(2);
	}
	
	/**
	 * Gets the vertical look axis
	 * 
	 * @return	Vertical look movement
	 */
	public static float getVerticalAxisLook() {
		return glfwGetJoystickAxes(GLFW_JOYSTICK_1).get(3);
	}
	
	/**
	 * Gets if the button is down
	 * 
	 * @param buttonID	ID of button
	 * @return			Is the button down
	 */
	public static boolean isButtonDown(int buttonID) {
		if(!XBOXController.isControllerConnected()) return false;
		return glfwGetJoystickButtons(XBOXController.GLFW_JOYSTICK_1).get(buttonID) == 1;
	}
	
	/**
	 * Gets the depression amount of the left trigger
	 * 
	 * @return The amount that the left trigger is pressed
	 */
	public static float leftTriggerPower() {
		return (glfwGetJoystickAxes(XBOXController.GLFW_JOYSTICK_1).get(4) + 1) / 2;
	}
	
	/**
	 * Gets the depression amount of the right trigger
	 * 
	 * @return The amount that the right trigger is pressed
	 */
	public static float rightTriggerPower() {
		return (glfwGetJoystickAxes(XBOXController.GLFW_JOYSTICK_1).get(5) + 1) / 2;
	}

	/**
	 * Gets if the controller is connected
	 * 
	 * @return	Is the controller connected
	 */
	public static boolean isControllerConnected() {
		return glfwJoystickPresent(XBOXController.GLFW_JOYSTICK_1);
	}

}
