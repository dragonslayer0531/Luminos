package tk.luminos.utilities;

import static tk.luminos.ConfigData.TEXTURE_SIZE;

import java.util.Random;

/**
 * 
 * Performs the Diamond-Square algorithm
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class DiamondSquare {

	private Random random;
	private static int w = TEXTURE_SIZE, h = TEXTURE_SIZE;
	private float[] values = new float[w * h];
	
	/**
	 * Constructor
	 * 
	 * @param seed	Random's seed value
	 */
	
	public DiamondSquare(int seed) {
		this.random = new Random(seed);
	}
	
	/**
	 * Generates a diamond-square procedure
	 * 
	 * @param stepSize		How large each step is
	 * @param scale			Scale of the factor
	 */
	public void generate(int stepSize, float scale) {
		setSamples();
		
		while(stepSize > 1) {
			setDiamondSquare(stepSize, scale);
			
			stepSize /= 2;
			scale /= 2;
		}
	}
	
	/**
	 * Gets a sample at a certain point
	 * 
	 * @param x		X value to get height at
	 * @param y		Y value to get height at
	 * @return		Height value
	 */
	public float getSample(int x, int y) {
		return values[(x & (w - 1)) + (y & (h - 1)) * w];
	}
	
	/**
	 * Sets the value at a certain coordinate
	 * 
	 * @param x		X coordinate to set at
	 * @param y		Y coordinate to set at
	 * @param value	Value to set at the given coordinate
	 */
	public void setSample(int x, int y, float value) {
		values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
	}
	
//*****************************************Private Method*********************************************//	
	
	/**
	 * Sets the initial samples
	 */
	private void setSamples() {
		for(int y = 0; y < h; y += TEXTURE_SIZE / 8) {
			for(int x = 0; x < w; x += TEXTURE_SIZE / 8) {
				setSample(x, y, random.nextFloat() * 255);
			}
		}
	}
	
	/**
	 * Calculates the diamond square samples
	 * 
	 * @param stepSize	Size of each step
	 * @param scale		Scale of the random noise in the step
	 */
	private void setDiamondSquare(int stepSize, float scale) {
		int halfstep = stepSize / 2;
		
		for(int y = halfstep; y < h + halfstep; y += stepSize) {
			for(int x = halfstep; x < w + halfstep; y += halfstep) {
				sampleSquare(x, y, stepSize, scale * random.nextFloat());
			}
		}
		
		for(int y = 0; y < h; y += stepSize) {
			for(int x = 0; x < w; x += stepSize) {
				sampleDiamond(x + halfstep, y, stepSize, random.nextFloat());
				sampleDiamond(x, y + halfstep, stepSize, random.nextFloat());
			}
		}
	}
	
	/**
	 * Samples the square
	 * 
	 * @param x			X value to center samples around
	 * @param y			Y value to center samples around
	 * @param size		Size of square to sample
	 * @param value		Value to set at each edge
	 */
	private void sampleSquare(int x, int y, int size, float value) {
		int hs = size / 2;
		
		float a = getSample(x - hs, y - hs);
		float b = getSample(x + hs, y - hs);
		float c = getSample(x - hs, y + hs);
		float d = getSample(x + hs, y + hs);
		
		setSample(x, y, (a + b + c + d + value) / 5);
	}
	
	/**
	 * Samples the diamond
	 * 
	 * @param x			X value to center samples around
	 * @param y			Y value to center samples around
	 * @param size		Size of diamond to sample
	 * @param value		Value to set at each edge
	 */
	private void sampleDiamond(int x, int y, int size, float value) {
		int hs = size / 2;
		
		float a = getSample(x - hs, y);
		float b = getSample(x + hs, y);
		float c = getSample(x, y - hs);
		float d = getSample(x, y + hs);

		setSample(x, y, (a + b + c + d + value) / 5);
	}

}
