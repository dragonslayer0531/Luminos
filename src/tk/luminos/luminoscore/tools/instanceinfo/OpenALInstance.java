package tk.luminos.luminoscore.tools.instanceinfo;

import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL;

/**
 * 
 * Gets OpenAL Instance Information
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class OpenALInstance {
	
	/**
	 * Gets OpenAL version in format: Major.Minor
	 * 
	 * @return	OpenAL format
	 */
	public static String getOpenALVersion() {
		return AL10.alGetString(AL10.AL_VERSION);
	}
	
	/**
	 * Gets OpenAL driver vendor
	 * 
	 * @return	OpenAL driver vendor
	 */
	public static String getVendor() {
		return AL10.alGetString(AL10.AL_VENDOR);
	}
	
	/**
	 * Gets OpenAL driver data
	 * 
	 * @return	OpenAL driver data
	 */
	public static String getRenderer() {
		return AL10.alGetString(AL10.AL_RENDERER);
	}
	
	/**
	 * Gets OpenAL instance extensions
	 * 
	 * @return		OpenAL extensions
	 */
	public static String getOpenALExtensions() {
		return AL10.alGetString(AL10.AL_EXTENSIONS);
	}
	
	/**
	 * Compiles OpenAL context information
	 * 
	 * @return		OpenAL context information
	 */
	public static String getContextInformation() {
		assert(GL.getCapabilities() != null);
		StringBuilder contextInfo = new StringBuilder();
		contextInfo.append(new String("OPENAL VERSION: ") + getOpenALVersion());
		appendNewLine(contextInfo);
		contextInfo.append(new String("SOUND DRIVER VENDOR: ") + getVendor());
		appendNewLine(contextInfo);
		contextInfo.append(new String("SOUND RENDERER: ") + getRenderer());
		appendNewLine(contextInfo);
		if(getOpenALExtensions() != null) {
			contextInfo.append(new String("OPENAL EXTENSIONS: ") + getOpenALExtensions());
		} else {
			contextInfo.append(new String("OPENAL EXTENSIONS: ") + "null");
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
