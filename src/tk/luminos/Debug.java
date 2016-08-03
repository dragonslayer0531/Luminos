package tk.luminos;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import tk.luminos.tools.instanceinfo.GLFWInstance;
import tk.luminos.tools.instanceinfo.JavaEnvironmentInstance;
import tk.luminos.tools.instanceinfo.OpenGLInstance;

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
	private static StringBuilder header = new StringBuilder();
	
	public static void prepare() {
		header.append(JavaEnvironmentInstance.getEnvironmentData());
		header.append(OpenGLInstance.getContextInformation());
		header.append(GLFWInstance.getContextInformation());
	}
	
	/**
	 * Append string to debug buffer
	 * 
	 * @param string	String to be added
	 */
	public static void addData(String string) {
		appendNewLine(debug_data);
		if(DEBUG) debug_data.append(string);
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
		if(debug_data.toString() != null) {
			try {
				FileWriter fw = new FileWriter("DEBUG" + ManagementFactory.getRuntimeMXBean().getName() + ".lof", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw);
				pw.write(header.toString());
				pw.write(debug_data.toString());
				pw.flush();
				pw.close();
				bw.close();
				fw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
//***********************************Private Methods*******************************************//
	
	/**
	 * Appends new line to string builder
	 * 
	 * @param sb		String builder to append to
	 */
	private static void appendNewLine(StringBuilder sb) {
		sb.append(System.lineSeparator());
	}
	

}
