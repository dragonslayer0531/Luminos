package tk.luminos.graphics.display;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import tk.luminos.maths.Vector2;

/**
 * 
 * Gathers data on the defined input device, such as
 * refresh rate, bit depth, and dimensions
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Device {
	
	private GraphicsEnvironment ge;
	private GraphicsDevice screen;
	
	private int refreshRate;
	private int bitDepth;
	private int colors;
	private Vector2 dimensions;
	
	/**
	 * Initializes the default device
	 * 
	 * @throws Exception		Thrown when device is unknown
	 */
	public Device() throws Exception {
		init(-1);
	}
	
	/**
	 * Initializes to a specified device
	 * 
	 * @param deviceID			ID number of the device
	 * @throws Exception		Thrown when device is unknown
	 */
	public Device(int deviceID) throws Exception {
		init(deviceID);
	}
	
	private void init(int deviceID) throws Exception {
		if (deviceID < -1)
			throw new Exception("Unknown Device Handle");
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		if (deviceID != -1) {
			screen = ge.getScreenDevices()[deviceID];
		}
		else {
			screen = ge.getDefaultScreenDevice();
		}
		
		DisplayMode mode = screen.getDisplayMode();
		refreshRate = mode.getRefreshRate();
		bitDepth = mode.getBitDepth();
		colors = (int) Math.pow(2, bitDepth);
		dimensions = new Vector2(mode.getWidth(), mode.getHeight());
	}

	/**
	 * Gets the refresh rate of the current device
	 * 
	 * @return		Refresh rate of current device
	 */
	public int getRefreshRate() {
		return refreshRate;
	}

	/**
	 * Gets the bit depth of the current deivce
	 * 
	 * @return		Bit depth of current device
	 */
	public int getBitDepth() {
		return bitDepth;
	}

	/**
	 * Gets the color range of the current device
	 * 
	 * @return		Color range of current device
	 */
	public int getColors() {
		return colors;
	}

	/**
	 * Gets the dimensions of the current device
	 * 
	 * @return		Dimensions of current device
	 */
	public Vector2 getDimensions() {
		return dimensions;
	}
	
	/**
	 * Creates string of data representing the device
	 * 
	 * @return 		Data about device
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Refresh Rate: " + refreshRate + System.lineSeparator());
		builder.append("Bit Depth:    " + bitDepth + System.lineSeparator());
		builder.append("Dimensions:   " + dimensions.x + " x " + dimensions.y);
		return builder.toString();
	}

}
