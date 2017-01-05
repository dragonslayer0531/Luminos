package com.luminos.graphics.display;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.luminos.tools.maths.vector.Vector2f;

public class Device {
	
	private GraphicsEnvironment ge;
	private GraphicsDevice screen;
	
	private int refreshRate;
	private int bitDepth;
	private int colors;
	private Vector2f dimensions;
	
	public Device() throws Exception {
		init(-1);
	}
	
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
		dimensions = new Vector2f(mode.getWidth(), mode.getHeight());
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public int getColors() {
		return colors;
	}

	public Vector2f getDimensions() {
		return dimensions;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Refresh Rate: " + refreshRate + System.lineSeparator());
		builder.append("Bit Depth:    " + bitDepth + System.lineSeparator());
		builder.append("Colors:       " + colors + System.lineSeparator());
		builder.append("Dimensions:   " + dimensions.x + "," + dimensions.y);
		return builder.toString();
	}

}
