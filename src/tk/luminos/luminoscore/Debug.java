package tk.luminos.luminoscore;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

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
	
	private static final String NEW_LINE = System.lineSeparator();
	
	private static StringBuilder debug_data = new StringBuilder();
	
	/**
	 * Append string to debug buffer
	 * 
	 * @param string	String to be added
	 */
	public static void addData(String string) {
		if(DEBUG) debug_data.append(string + NEW_LINE);
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
		if(debug_data.toString() == null) {
			try {
				PrintWriter pw = new PrintWriter("DEBUG" + ManagementFactory.getRuntimeMXBean().getName() + ".lof");
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
	 * Retrieves Runtime Environment Data
	 * 
	 * @return Runtime Environment Data
	 */
	private static String getEnvironmentData() {
		StringBuilder data = new StringBuilder();
		
		data.append(System.getProperty("os.name"));
		data.append(NEW_LINE);
		data.append("MaxMem " + Runtime.getRuntime().maxMemory());
		data.append(NEW_LINE);
		data.append("UsedMem " + Runtime.getRuntime().totalMemory());
		data.append(NEW_LINE);
		data.append("ActiveThreadCount " + Thread.activeCount());
		data.append(NEW_LINE);
		data.append("CurrentThread " + Thread.currentThread());
		data.append(NEW_LINE);
		
		return data.toString();
	}

}
