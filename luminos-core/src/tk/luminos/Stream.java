package tk.luminos;

import java.io.PrintStream;

/**
 * Creates an output stream
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class Stream {
	
	private StringBuilder builder = new StringBuilder();
	private static String HEADER;
	
	/**
	 * Creates default stream object
	 */
	public Stream() {
		HEADER = "LUMINOS_STREAM";
		builder.append(HEADER + "_BEGIN\n");
	}
	
	/**
	 * Creates stream object with custom header
	 * 
	 * @param header	stream header
	 */
	public Stream(String header) {
		HEADER = header;
		builder.append(HEADER + "_BEGIN\n");
		builder.append(System.lineSeparator());
	}
	
	/**
	 * Appends bytes to stream
	 * 
	 * @param data	bytes to to add
	 */
	public void append(byte[] data) {
		builder.append(new String(data));
		builder.append(System.lineSeparator());
	}
	
	/**
	 * Appends {@link Throwable} to stream
	 * 
	 * @param throwable 	throwable to add
	 */
	public void append(Throwable throwable) {
		for (StackTraceElement ste : throwable.getStackTrace()) {
			builder.append(ste.toString());
			builder.append(System.lineSeparator());
		}
	}
	
	/**
	 * Appends {@link String} to stream
	 * 
	 * @param data	string to add
	 */
	public void append(String data) {
		builder.append(data);
		builder.append(System.lineSeparator());
	}
	
	/**
	 * Clears the stream
	 */
	public void flush() {
		builder = new StringBuilder();
	}
	
	/**
	 * String of stream contents
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		builder.append(HEADER + "_END");
		return builder.toString();
	}
	
	/**
	 * Prints stream to console
	 */
	public void print() {
		System.out.println(toString());
	}
	
	/**
	 * Prints stream to file
	 * 
	 * @param stream	Output stream
	 */
	public void print(PrintStream stream) {
		stream.println(toString());
	}

}
