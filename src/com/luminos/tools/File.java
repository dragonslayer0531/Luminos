package com.luminos.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class File {

	private static final String FILE_DELIMITER = "/";

	private String path;
	private String name;

	public File(String filename) {
		this.path = FILE_DELIMITER + filename;
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	public File(String[] paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_DELIMITER + part);
		}
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	public File(File file, String subfile) {
		this.path = file.path + FILE_DELIMITER + subfile;
		this.name = subfile;
	}

	public File(File file, String[] subfiles) {
		this.path = file.path;
		for (String part : subfiles) {
			this.path += (FILE_DELIMITER + part);
		}
		String[] dirs = path.split(FILE_DELIMITER);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	public InputStream getInputStream() {
		return Class.class.getResourceAsStream(path);
	}

	public BufferedReader getReader() throws Exception {
		InputStreamReader isr = new InputStreamReader(getInputStream());
		BufferedReader reader = new BufferedReader(isr);
		return reader;
	}
	
	public String getName() {
		return name;
	}

}