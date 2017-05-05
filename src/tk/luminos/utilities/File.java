package tk.luminos.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * File utility
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class File {

	private static final String FILE_DELIMITER = "/";

	private String path;
	private String name;

	/**
	 * Creates file
	 * 
	 * @param filename		Name/Path of file
	 */
	public File(String filename) {
		this.path = FILE_DELIMITER + filename;
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	/**
	 * Creates file
	 * 
	 * @param paths	to file
	 */
	public File(String[] paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_DELIMITER + part);
		}
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	/**
	 * Creates file
	 * 
	 * @param file		File directory
	 * @param subfile	File location
	 */
	public File(File file, String subfile) {
		this.path = file.path + FILE_DELIMITER + subfile;
		this.name = subfile;
	}

	/**
	 * Creates file
	 * 
	 * @param file		File directory
	 * @param subfiles	File locations
	 */
	public File(File file, String[] subfiles) {
		this.path = file.path;
		for (String part : subfiles) {
			this.path += (FILE_DELIMITER + part);
		}
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	/**
	 * Gets file path
	 * 
	 * @return	File path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets string of file path
	 * 
	 * @return string	File path
	 */
	@Override
	public String toString() {
		return getPath();
	}

	/**
	 * Gets file as an input stream
	 * 
	 * @return		Input stream of file path
	 */
	public InputStream getInputStream() {
		return Class.class.getResourceAsStream(path);
	}

	/**
	 * Gets file reader
	 * 
	 * @return		File reader
	 * @throws Exception		Thrown if reader cannot be created
	 */
	public BufferedReader getReader() throws Exception {
		InputStreamReader isr = new InputStreamReader(getInputStream());
		BufferedReader reader = new BufferedReader(isr);
		return reader;
	}
	
	/**
	 * Gets name of file
	 * 
	 * @return	Name of file
	 */
	public String getName() {
		return name;
	}

}