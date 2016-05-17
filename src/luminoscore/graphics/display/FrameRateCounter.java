package luminoscore.graphics.display;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * The protected class keeping track of FPS
 *
 */

public class FrameRateCounter {
	
	private int start;
	private int end;
	private int frameTime = 0;
	private short frames = 0;
	private int fps;
	private float frameTimeCount;

	/**
	 * Constructor of the frame rate counter
	 */
	protected FrameRateCounter() {
	}
	
	/**
	 * Starts the frame rate counter.  Called at beginning of each GLFW update
	 */
	protected void start() {
		start = (int) System.currentTimeMillis();
	}
	
	/**
	 * Ends the frame time counter and calculates.  Called at end of each GLFW update
	 */
	protected void calculate() {
		end = (int) System.currentTimeMillis();
		frameTime = frameTime + end - start;
		frameTimeCount = (end - start);
		frameTimeCount /= 1000;
		++frames;
		if(frameTime >= (1000)) {
			fps = frames;
			frames = 0;
			frameTime = 0;
		}
		
	}
	
	/**
	 * Gets the frames per second count
	 * 
	 * @return Value of FPS 
	 */
	protected int getFPS() {
		return fps;
	}
	
	/**
	 * Gets the length of time per frame
	 * 
	 * @return Value of seconds per frame
	 */
	protected float getFrameTime() {
		return frameTimeCount;
	}
	
}
