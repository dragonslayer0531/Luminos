package luminoscore.graphics.display;

public class FrameRateCounter {
	
	/*
	 * Author: Nick Clark
	 * Created On: 12/28/15
	 * Updated On: 12/29/15
	 * Update Info: Added documentation and FrameTime getter
	 */
	
	private int start;
	private int end;
	private int frameTime = 0;
	private short frames = 0;
	private int fps;
	private int refresh_interval = 1;
	private float frameTimeCount;
	
	/*
	 * Creates a FrameTimeCounter.  It can be used for determining FPS and how quickly each frame loads
	 * @param refresh_interval How fast the FPS counter refreshes.
	 */
	
	public FrameRateCounter(int refresh_interval) {
		this.refresh_interval = refresh_interval;
	}
	
	//Starts the FrameTimeCounter.  To be called at the beginning of the render loop.
	
	public void start() {
		start = (int) System.currentTimeMillis();
	}
	
	//Calculates the values of FrameTime and FPS at the momement of it being called.  Should be called at the end of the render loop.
	
	public void calculate() {
		end = (int) System.currentTimeMillis();
		frameTime = frameTime + end - start;
		frameTimeCount = (end - start);
		frameTimeCount /= 1000;
		++frames;
		if(frameTime >= (1000 * refresh_interval)) {
			fps = frames / refresh_interval;
			frames = 0;
			frameTime = 0;
		}
		
	}
	
	//Getter methods
	
	public int getFPS() {
		return fps;
	}
	
	public float getFrameTime() {
		return frameTimeCount;
	}
	
}
