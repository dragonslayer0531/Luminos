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
	 * @return Luminos	New Luminos Instance
	 * 
	 * Invokes Luminos instance
	 */
	public static Luminos createLuminosInstance() {
		if(!GlobalLock.INITIATED) {
			GlobalLock.INITIATED = true;
		}
		return new Luminos();
	}
	
	/**
	 * @param windowID		Window ID
	 * 
	 * Sets the window ID
	 */
	public void setWindowID(long windowID) {
		this.windowID = windowID;
	}

}
