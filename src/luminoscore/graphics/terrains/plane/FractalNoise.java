package luminoscore.graphics.terrains.plane;

import java.util.Random;

import luminoscore.util.math.Interpolation;

public class FractalNoise {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/17/2016
	 */
	
	//Constructor Data
	private int seed;
	private Random random = new Random();
	private int xOffset = 0, zOffset = 0;

	//Calculation Data
	private static float amplitude = 150, roughness = .1f;
	private static int octaves = 4;

	/*
	 * @param gridX Defines the left bound
	 * @param gridZ Defines the lower bound
	 * @param vertexCount Defines the number of vertices to be generated
	 * @param seed Defines seed for random
	 */
	public FractalNoise(int gridX, int gridZ, int vertexCount, int seed) {
		this.seed = seed;
		this.xOffset = gridX * (vertexCount - 1);
		this.zOffset = gridZ * (vertexCount - 1);
	}
	
	/*
	 * @param x Defines X
	 * @param Z Defines Z
	 * @return float
	 * 
	 * Generates height for a value
	 */
	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2,  octaves - 1);
		for(int i = 0; i < octaves; i++) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(roughness, i) * amplitude;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}
		
		return total;
	}
	
	/*
	 * @param x Defines X
	 * @param z Defines Z
	 * @return float
	 * 
	 * Interpolates surrounding noise
	 */
	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		
		float i1 = Interpolation.cosineInterpolation(v1, v2, fracX);
		float i2 = Interpolation.cosineInterpolation(v3, v4, fracX);
		
		return Interpolation.cosineInterpolation(i1, i2, fracZ);
	}
	
	/*
	 * @param x Defines X
	 * @param Z Defines Z
	 * @return float
	 * 
	 * Creates smoothed noise based on adjacent values
	 */
	private float getSmoothNoise(int x, int z) {
		float cornerSum = (getNoise(x - 1, z - 1) + 
				getNoise(x - 1, z + 1) +
				getNoise(x + 1, z + 1) + 
				getNoise(x + 1, z - 1)) / 16;
		float sideSum = (getNoise(x, z - 1) + 
				getNoise(x, z + 1) +
				getNoise(x - 1, z) +
				getNoise(x + 1, z)) / 8;
		float center = getNoise(x, z) / 4;
		return cornerSum + sideSum + center;
	}
	
	/*
	 * @param x Defines X
	 * @param z Defines Z
	 * @return float
	 * 
	 * Randomly generates consistant value for X and Z
	 */
	private float getNoise(int x, int z) {
		random.setSeed(Math.abs(x * 49632) + Math.abs(z * 325176) + seed);
		return random.nextFloat() * 2f - 1;
	}

}
