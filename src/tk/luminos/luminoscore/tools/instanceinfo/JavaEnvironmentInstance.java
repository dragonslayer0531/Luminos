package tk.luminos.luminoscore.tools.instanceinfo;

/**
 * 
 * Gets Java Runtime Environment Information
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class JavaEnvironmentInstance {
	
	/**
	 * Retrieves Runtime Environment Data
	 * 
	 * @return Runtime Environment Data
	 */
	public static String getEnvironmentData() {
		StringBuilder data = new StringBuilder();
		data.append(System.getProperty("os.name"));
		appendNewLine(data);
		data.append("MaxMem " + Runtime.getRuntime().maxMemory());
		appendNewLine(data);
		data.append("UsedMem " + Runtime.getRuntime().totalMemory());
		appendNewLine(data);
		data.append("ActiveThreadCount " + Thread.activeCount());
		appendNewLine(data);
		data.append("CurrentThread " + Thread.currentThread());
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
