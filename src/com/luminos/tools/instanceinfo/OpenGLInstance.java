package com.luminos.tools.instanceinfo;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

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
		assert(GL.getCapabilities() != null);
		return GL11.glGetString(GL11.GL_VERSION);
	}
	
	/**
	 * Gets the Graphics Card Manufacturer/Vendor
	 * 
	 * @return		Graphics Card Manufacturer/Vendor
	 */
	public static String getGraphicsCardManufacturer() {
		assert(GL.getCapabilities() != null);
		return GL11.glGetString(GL11.GL_VENDOR);
	}
	
	/**
	 * Gets the Rendering Graphics Card
	 * 
	 * @return		Graphics card in use
	 */
	public static String getRenderer() {
		assert(GL.getCapabilities() != null);
		return GL11.glGetString(GL11.GL_RENDERER);
	}
	
	/**
	 * Gets all extensions in use by OpenGL
	 * 
	 * @return		Extensions in use by OpenGL
	 */
	public static String getOpenGLExtensions() {
		assert(GL.getCapabilities() != null);
		return GL11.glGetString(GL11.GL_EXTENSIONS);
	}
	
	/**
	 * Compiles OpenGL context information
	 * 
	 * @return		OpenGL context information
	 */
	public static String getContextInformation() {
		assert(GL.getCapabilities() != null);
		StringBuilder contextInfo = new StringBuilder();
		contextInfo.append(new String("OPENGL VERSION: ") + getOpenGLVersion());
		appendNewLine(contextInfo);
		contextInfo.append(new String("GRAPHICS CARD MANUFACTURER: ") + getGraphicsCardManufacturer());
		appendNewLine(contextInfo);
		contextInfo.append(new String("RENDERING GRAPHICS CARD: ") + getRenderer());
		appendNewLine(contextInfo);
		if(getOpenGLExtensions() != null) {
			contextInfo.append(new String("OPENGL EXTENSIONS: ") + getOpenGLExtensions());
		} else {
			contextInfo.append(new String("OPENGL EXTENSIONS: ") + "null");
		}
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
