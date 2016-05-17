package luminoscore;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Instantiates Luminos Instance
 *
 */

public class Luminos {
	
	protected long windowID;
	
	/**
	 * Construction
	 */
	private Luminos() {}
	
	/**
	 * Invokes a new Luminos instance
	 * 
	 * @return		New Luminos Instance
	 */
	public static Luminos createLuminosInstance() {
		if(!GlobalLock.INITIATED) {
			GlobalLock.INITIATED = true;
		}
		
		return new Luminos();
	}
	
	/**
	 * Sets the window ID
	 * 
	 * @param windowID		Window ID
	 */
	public void setWindowID(long windowID) {
		this.windowID = windowID;
	}

	/**
	 * Closes the instance
	 */
	public void close() {
		GlobalLock.INITIATED = false;
		GlobalLock.printToFile("config.xml");
	}

}
