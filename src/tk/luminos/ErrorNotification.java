package tk.luminos;

import javax.swing.JOptionPane;

/**
 * Allows for visual error handling 
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class ErrorNotification {
	
	public static final int INFORMATION = 0x0;
	public static final int WARNING = 0x1;
	public static final int ERROR = 0x2;
	
	private static final int MAX = ERROR;
	
	public static String WARNING_TITLE = "WARNING";
	public static String ERROR_TITLE = "ERROR";
	
	/**
	 * Opens notification with error message
	 * 
	 * @param error			error message
	 * @param severity		severity of message
	 */
	public static void notify(String error, int severity) {
		assert(severity > 0 && severity <= MAX);
		if (severity == INFORMATION) 
		{
			JOptionPane.showMessageDialog(null, error);
		}
		else if (severity == WARNING)
		{
			JOptionPane.showMessageDialog(null, error, WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
		}
		else if (severity == ERROR)
		{
			JOptionPane.showMessageDialog(null, error, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		else
		{
			throw new RuntimeException("Unsupported operation");
		}
	}
	
}
