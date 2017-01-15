package tk.luminos.filesystem.plaintext;

import static tk.luminos.filesystem.plaintext.DataStruct.INT;
import static tk.luminos.filesystem.plaintext.DataStruct.STRING;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import tk.luminos.Debug;

/**
 * Creates FileWriter specialized for Luminos
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class FileWriter {
	
	private static final String HEADER = ">LUMINOS_ENGINE";
	private static final String FOOTER = "LUMINOS_ENGINE<";
	private static final float VERSION = 1.0f;
	
	/**
	 * Delimiter character
	 */
	public static final String DELIM = "::";
	
	private String fileName;
	private PrintWriter pw;
	
	/**
	 * Creates new file writer
	 * 
	 * @param fileName		Name of file
	 */
	public FileWriter(String fileName) {
		this.fileName = fileName;
		
	}
	
	/**
	 * Opens file and writes header
	 */
	public void open() {
		try {
			pw = new PrintWriter(fileName);
			this.write(new PlainTextObject(STRING, "HEADER", HEADER));
			this.write(new PlainTextObject(INT, "VERSION", VERSION));
		} catch (FileNotFoundException e) {
			Debug.addData(e);
		}
	}
	
	/**
	 * Writes {@link PlainTextObject} to file
	 * 
	 * @param obj		Object to write
	 */
	public void write(PlainTextObject obj) {
		pw.write(obj.type.getID() + DELIM + obj.name + DELIM + obj.value.toString() + System.lineSeparator());
	}
	
	/**
	 * Writes footer and closes file
	 */
	public void close() {
		this.write(new PlainTextObject(STRING, "FOOTER", FOOTER));
		pw.flush();
		pw.close();
	}

}
