package tk.luminos;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import tk.luminos.utilities.DateUtils;
import tk.luminos.utilities.GLFWInstance;
import tk.luminos.utilities.JavaEnvironmentInstance;
import tk.luminos.utilities.OpenGLInstance;

/**
 * 
 * Custom Luminos Debugger
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class Debug {
		
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
	 * @param e		Throwable error
	 */
	public static void addData(Throwable e) {
		appendNewLine(e, debug_data);
	}
	
	/**
	 * Prints to console
	 */
	public static void out() {
		System.out.println(debug_data.toString());
	}
	
	/**
	 * Prints to file
	 */
	public static void print() {
		if(debug_data.toString() != null) {
			try {
				FileWriter fw = new FileWriter("DEBUG" + DateUtils.getDateTime() + ".lof", true);
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
	private static void appendNewLine(Throwable e, StringBuilder sb) {
		sb.append(e.getMessage());
		sb.append(System.lineSeparator());
	}
	

}
