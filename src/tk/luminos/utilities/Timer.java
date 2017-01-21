package tk.luminos.utilities;

/**
 * 
 * @author Nick Clark
 *
 */
public class Timer {

	private double lastLoopTime;

	/**
	 * Initializes timer
	 */
	public void init() {
		lastLoopTime = getTime();
	}

	/**
	 * Gets time of application
	 * 
	 * @return	System time in nanoseconds
	 */
	public double getTime() {
		return System.nanoTime() / 1_000_000_000.0;
	}

	/**
	 * Gets time between last query and current time
	 * 
	 * @return		Elapsed time
	 */
	public float getElapsedTime() {
		double time = getTime();
		float elapsedTime = (float) (time - lastLoopTime);
		lastLoopTime = time;
		return elapsedTime;
	}

	/**
	 * Gets the time that it took for the last update
	 * 
	 * @return		Last update time
	 */
	public double getLastLoopTime() {
		return lastLoopTime;
	}
}