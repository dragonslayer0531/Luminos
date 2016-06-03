package tk.luminos.luminoscore.tools.instanceinfo;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * 
 * Gets GLFW Instance Information
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GLFWInstance {
	
	/**
	 * Gets the GLFW Version in format: Major.Minor.Patch
	 * 
	 * @return	GLFW Version
	 */
	public static String getVersion() {
		assert(GLFW.glfwInit() == GL11.GL_TRUE);
		return GLFW.glfwGetVersionString();
	}
	
	/**
	 * Compile GLFW Context information
	 * 
	 * @return	GLFW Context info
	 */
	public static String getContextInformation() {
		assert(GLFW.glfwInit() == GL11.GL_TRUE);
		StringBuilder data = new StringBuilder();
		data.append(new String("GLFW VERSION: ") + getVersion());
		appendNewLine(data);
		return data.toString();
	}
	
	/**
	 * Appends new line to string builder
	 * 
	 * @param sb		String builder to append to
	 */
	private static void appendNewLine(StringBuilder sb) {
		sb.append(System.lineSeparator());
	}

}
