package luminoscore;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Custom Luminos Engine Exception
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
		Debug.addData(message);
	}

}
