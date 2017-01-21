package tk.luminos.utilities.instanceinfo;

import static org.lwjgl.opengl.GL.getCapabilities;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

/**
 * 
 * Gets OpenGL Instance Information
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class OpenGLInstance {
	
	/**
	 * Gets OpenGL Version in format: Major.Minor.Patch - Build Version
	 * 
	 * @return		OpenGL version
	 */
	public static String getOpenGLVersion() {
		assert(getCapabilities() != null);
		return glGetString(GL_VERSION);
	}
	
	/**
	 * Gets the Graphics Card Manufacturer/Vendor
	 * 
	 * @return		Graphics Card Manufacturer/Vendor
	 */
	public static String getGraphicsCardManufacturer() {
		assert(getCapabilities() != null);
		return glGetString(GL_VENDOR);
	}
	
	/**
	 * Gets the Rendering Graphics Card
	 * 
	 * @return		Graphics card in use
	 */
	public static String getRenderer() {
		assert(getCapabilities() != null);
		return glGetString(GL_RENDERER);
	}
	
	/**
	 * Compiles OpenGL context information
	 * 
	 * @return		OpenGL context information
	 */
	public static String getContextInformation() {
		assert(getCapabilities() != null);
		StringBuilder contextInfo = new StringBuilder();
		contextInfo.append(new String("OPENGL VERSION: ") + getOpenGLVersion());
		appendNewLine(contextInfo);
		contextInfo.append(new String("GRAPHICS CARD MANUFACTURER: ") + getGraphicsCardManufacturer());
		appendNewLine(contextInfo);
		contextInfo.append(new String("RENDERING GRAPHICS CARD: ") + getRenderer());
		appendNewLine(contextInfo);
		
		return contextInfo.toString();
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
