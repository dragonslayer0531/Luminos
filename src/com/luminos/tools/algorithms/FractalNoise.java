package com.luminos.tools.algorithms;

import java.util.Random;

import com.luminos.graphics.terrains.TerrainType;
import com.luminos.tools.Maths;

/**
 * 
 * Performs the fractal noise algorithm
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class FractalNoise {
	
	private int seed;
	private Random random = new Random();
	private int xOffset = 0;
	private int zOffset = 0;
	
	public float amplitude = 100;
	private float octaves = 4;
	private float roughness = .1f;
	
	/**
	 * Constructor
	 * 
	 * @param seed	Seed to be used in random
	 */
	public FractalNoise(int seed) {
		this.seed = seed;
	}
	
	/**
	 * Constructor
	 * 
	 * @param gridX			Terrain Grid X Begin
	 * @param gridZ			Terrain Grid Z Begin
	 * @param vertexCount	Vertex Count of Noise
	 * @param seed			Seed used by random
	 * @param type			Type of terrain
	 */
	public FractalNoise(int gridX, int gridZ, int vertexCount, int seed, TerrainType.Type type) {
        this.seed = seed;
        xOffset = gridX * (vertexCount-1) + 50000;
        zOffset = gridZ * (vertexCount-1) + 50000;
        float[] data = TerrainType.getData(type);
        this.amplitude = data[0];
        this.octaves = data[1];
        this.roughness = data[2];
    }
	
	/**
	 * Calculates height at given point
	 * 
	 * @param x			X value of generation
	 * @param z			Z value of generation
	 * @return 			Generated height
	 */
	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, octaves - 1);
		for(int i = 0; i < octaves; i++) {
			float freq = (float) (Math.pow(2,  i) / d);
			float amp = (float) Math.pow(roughness, i) * amplitude;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}

		float max = amplitude * 2;
		float cur = total + amplitude;
		float blend = cur / max;
		return total * blend;
	}
	
//**************************************************Private Methods**********************************************//
	
	/**
	 * Interpolates noise values
	 * 
	 * @param x			X value of interpolation
	 * @param z			Z value of interpolation
	 * @return 	Calculates interpolated noise
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
		
		float i1 = Maths.CosineInterpolation(v1, v2, fracX);
		float i2 = Maths.CosineInterpolation(v3, v4, fracX);
		return Maths.CosineInterpolation(i1, i2, fracZ);
	}
	
	/**
	 * Calculates smoothed noise
	 * 
	 * @param x			x value to calculate
	 * @param z			z value to calculate
	 * @return 			Smoothed noise value
	 */
	private float getSmoothNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + 
				getNoise(x - 1, z + 1) + 
				getNoise(x + 1, z - 1) +
				getNoise(x + 1, z + 1)) / 16;
		float sides = (getNoise(x - 1, z) + 
				getNoise(x + 1, z) +
				getNoise(x, z - 1) +
				getNoise(x, z + 1)) / 8;
		float center = getNoise(x, z) / 4;
		return corners + sides + center;
	}
	
	/**
	 * Calculates absolute noise value
	 * 
	 * @param x			x value to calculate
	 * @param z			z value to calculate
	 * @return 		 	Pure noise value
	 */
	private float getNoise(int x, int z) {
		random.setSeed(Math.abs(x * 49632) + Math.abs(z * 325176) + seed);
		return random.nextFloat() * 2f - 1f;
	}

}
