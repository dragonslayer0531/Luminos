package tk.luminos;

import java.io.PrintStream;

public class Stream {
	
	public StringBuilder builder = new StringBuilder();
	private static String HEADER;
	
	public Stream() {
		HEADER = "LUMINOS_STREAM";
		builder.append(HEADER + "_BEGIN");
	}
	
	public Stream(String header) {
		HEADER = header;
		builder.append(HEADER + "_BEGIN");
		builder.append(System.lineSeparator());
	}
	
	public void append(byte[] data) {
		builder.append(new String(data));
		builder.append(System.lineSeparator());
	}
	
	public void append(Throwable throwable) {
		for (StackTraceElement ste : throwable.getStackTrace()) {
			builder.append(ste.toString());
			builder.append(System.lineSeparator());
		}
	}
	
	public void append(String data) {
		builder.append(data);
		builder.append(System.lineSeparator());
	}
	
	public void flush() {
		builder = new StringBuilder();
	}
	
	@Override
	public String toString() {
		builder.append(HEADER + "_END");
		return builder.toString();
	}
	
	public void print() {
		System.out.println(toString());
	}
	
	public void print(PrintStream stream) {
		stream.println(toString());
	}

}
