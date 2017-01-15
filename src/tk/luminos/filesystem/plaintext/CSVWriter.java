package tk.luminos.filesystem.plaintext;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
	
	public static CSVWriter generate(String out) {
		return new CSVWriter(out);
	}
	
	public void appendData(String dat) {
		sb.append(dat);
		sb.append(System.lineSeparator());
	}
	
	public void appendData(String[] dat) {
		for (String d : dat) {
			sb.append(d);
			sb.append(DELIM);
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		sb.append(System.lineSeparator());
	}
	
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
