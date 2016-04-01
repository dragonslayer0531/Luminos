package luminoscore.graphics.terrains;

import java.util.Random;

public class PerlinNoise {
	
	private int seed;
	private Random random = new Random();
	private int xOffset = 0;
	private int zOffset = 0;
	
	private float amplitude = 50;
	private float octaves = 4;
	private float roughness = .1f;
	
	public PerlinNoise(int seed) {
		this.seed = seed;
	}
	
	public PerlinNoise(int gridX, int gridZ, int vertexCount, int seed, TerrainType.Type type) {
        this.seed = seed;
        xOffset = gridX * (vertexCount-1);
        zOffset = gridZ * (vertexCount-1);
        float[] data = TerrainType.getData(type);
        this.amplitude = data[0];
        this.octaves = data[1];
        this.roughness = data[2];
    }
	
	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, octaves - 1);
		for(int i = 0; i < octaves; i++) {
			float freq = (float) (Math.pow(2,  i) / d);
			float amp = (float) Math.pow(roughness, i) * amplitude;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}
		return total;
	}
	
	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}
	
	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float)(1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
	
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
	
	private float getNoise(int x, int z) {
		random.setSeed(Math.abs(x * 49632) + Math.abs(z * 325176) + seed);
		return random.nextFloat() * 2f - 1f;
	}

}
