package tk.luminos;

/**
 * 
 * Custom Luminos Engine Exception
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message		String to be printed to console
	 */
	public LuminosException(String message) {
		super(message);
	}

}
