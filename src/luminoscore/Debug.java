package luminoscore;

import java.io.FileNotFoundException;

import java.io.PrintWriter;

/**
 * 
 * Custom Luminos Debugger
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Debug {
	
	public static boolean DEBUG = true;
	public static boolean PRINT_TO_FILE = true;
	
	private static StringBuilder debug_data = new StringBuilder();
	
	/**
	 * Append string to debug buffer
	 * 
	 * @param string	String to be added
	 */
	public static void addData(String string) {
		debug_data.append(string + System.lineSeparator());
	}
	
	/**
	 * Prints to console
	 */
	public static void out() {
		System.out.println(debug_data.toString());
		System.exit(-1);
	}
	
	/**
	 * Prints to file
	 */
	public static void print() {
		if(debug_data.length() != 0) {
			try {
				PrintWriter pw = new PrintWriter("debug_out.lof");
				pw.write(getEnvironmentData());
				pw.write(debug_data.toString());
				pw.flush();
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
//***********************************Private Methods*******************************************//
	
	/**
	 * Retreives Runtime Environment Data
	 * 
	 * @return Runtime Environment Data
	 */
	private static String getEnvironmentData() {
		StringBuilder data = new StringBuilder();
		
		data.append(System.getProperty("os.name"));
		data.append(System.lineSeparator());
		data.append("MaxMem " + Runtime.getRuntime().maxMemory());
		data.append(System.lineSeparator());
		data.append("UsedMem " + Runtime.getRuntime().totalMemory());
		data.append(System.lineSeparator());
		data.append("ActiveThreadCount " + Thread.activeCount());
		data.append(System.lineSeparator());
		data.append("CurrentThread " + Thread.currentThread());
		data.append(System.lineSeparator());
		
		return data.toString();
	}

}
