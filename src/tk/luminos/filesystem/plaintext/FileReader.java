package tk.luminos.filesystem.plaintext;

import static tk.luminos.filesystem.plaintext.DataStruct.INVALID;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tk.luminos.Debug;

/**
 * 
 * Creates file reader specialized for Luminos
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class FileReader {
	
	private String fileName;
	private BufferedReader br;
	private List<PlainTextObject> ptos = new ArrayList<PlainTextObject>();
	
	/**
	 * Creates new File Reader
	 * 
	 * @param fileName	File to open
	 */
	public FileReader(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Opens file for reading
	 */
	public void open() {
		try {
			br = new BufferedReader(new java.io.FileReader(fileName));
		} catch (FileNotFoundException e) {
			Debug.addData(e);
		}
	}
	
	/**
	 * Reads file
	 */
	public void read() {
		try {
			String s = br.readLine();
			while (s != null) {
				String[] dat = s.split(FileWriter.DELIM);
				DataStruct type = DataStruct.getByID(Integer.parseInt(dat[0]));
				String name = dat[2];
				Object val;
				switch (type) {
				case INT:
					val = Integer.parseInt(dat[3]);
					break;
				case FLOAT:
					val = Float.parseFloat(dat[3]);
					break;
				case STRING:
					val = dat[3];
					break;
				case BOOLEAN:
					val = Boolean.getBoolean(dat[3]);
					break;
				default:
					val = INVALID;
					break;
				}
				ptos.add(new PlainTextObject(type, name, val));	
			}
		} catch (IOException e) {
			Debug.addData(e);
		}
		
	}
	
	/**
	 * Closes file
	 */
	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			Debug.addData(e);
		}
	}

}
