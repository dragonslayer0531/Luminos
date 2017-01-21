package tk.luminos.filesystem.plaintext;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * 
 * Allows for the creation of comma separated files
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class CSVWriter {

	private String out;
	private StringBuilder sb;
	private static final char DELIM = ',';
	
	private CSVWriter(String out) {
		if (!out.endsWith(".csv")) {
			this.out = out + ".csv";
		}
		else {
			this.out = out;
		}
		sb = new StringBuilder();
 	}
	
	/**
	 * Generates a new CSV Writer with a given file path
	 * 
	 * @param out		Output file path
	 * @return			New CSV Writer instance
	 */
	public static CSVWriter generate(String out) {
		return new CSVWriter(out);
	}
	
	/**
	 * Appends data to the CSV file
	 * 
	 * @param dat		Data to add to buffer
	 */
	public void appendData(String dat) {
		sb.append(dat);
		sb.append(System.lineSeparator());
	}
	
	/**
	 * Appends an array of data to the CSV file
	 * 
	 * @param dat		Data to add to buffer
	 */
	public void appendData(String[] dat) {
		for (String d : dat) {
			sb.append(d);
			sb.append(DELIM);
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append(System.lineSeparator());
	}
	
	/**
	 * Writes the buffer to the file
	 */
	public void writeToFile() {
		try {
			PrintWriter pw = new PrintWriter(out);
			pw.write(sb.toString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
