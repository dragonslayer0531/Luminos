package luminoscore.input;

import org.lwjgl.glfw.GLFW;

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
	
	public static float getHorizontalAxis(int controllerID) {
		return GLFW.glfwGetJoystickAxes(controllerID).get(0);
	}
	
	public static float getVerticalAxis(int controllerID) {
		return GLFW.glfwGetJoystickAxes(controllerID).get(1);
	}
	
	public static float getHorizontalAxisLook(int controllerID) {
		return GLFW.glfwGetJoystickAxes(controllerID).get(2);
	}
	
	public static float getVerticalAxisLook(int controllerID) {
		return GLFW.glfwGetJoystickAxes(controllerID).get(3);
	}
	
	public static boolean isButtonDown(int buttonID) {
		if(!XBOXController.isControllerConnected()) return false;
		return GLFW.glfwGetJoystickButtons(XBOXController.GLFW_JOYSTICK_1).get(buttonID) == 1;
	}
	
	public static float leftTriggerPower() {
		return (GLFW.glfwGetJoystickAxes(XBOXController.GLFW_JOYSTICK_1).get(4) + 1) / 2;
	}
	
	public static float rightTriggerPower() {
		return (GLFW.glfwGetJoystickAxes(XBOXController.GLFW_JOYSTICK_1).get(5) + 1) / 2;
	}

	public static boolean isControllerConnected() {
		return GLFW.glfwJoystickPresent(XBOXController.GLFW_JOYSTICK_1) == 1;
	}

}
