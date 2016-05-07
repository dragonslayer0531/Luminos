package luminoscore.tools.algorithms;

import java.util.Random;

import luminoscore.GlobalLock;

public class DiamondSquare {

	private Random random;
	private static int w = GlobalLock.TEXTURE_SIZE, h = GlobalLock.TEXTURE_SIZE;
	private float[] values = new float[w * h];
	
	public DiamondSquare(int seed) {
		this.random = new Random(seed);
	}
	
	public void generate(int stepSize, float scale) {
		setSamples();
		
		while(stepSize > 1) {
			setDiamondSquare(stepSize, scale);
			
			stepSize /= 2;
			scale /= 2;
		}
	}
	
	public float getSample(int x, int y) {
		return values[(x & (w - 1)) + (y & (h - 1)) * w];
	}
	
	public void setSample(int x, int y, float value) {
		values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
	}
	
	private void setSamples() {
		for(int y = 0; y < h; y += GlobalLock.TEXTURE_SIZE / 8) {
			for(int x = 0; x < w; x += GlobalLock.TEXTURE_SIZE / 8) {
				setSample(x, y, random.nextFloat() * 255);
			}
		}
	}
	
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
	
	private void sampleSquare(int x, int y, int size, float value) {
		int hs = size / 2;
		
		float a = getSample(x - hs, y - hs);
		float b = getSample(x + hs, y - hs);
		float c = getSample(x - hs, y + hs);
		float d = getSample(x + hs, y + hs);
		
		setSample(x, y, (a + b + c + d + value) / 5);
	}
	
	public void sampleDiamond(int x, int y, int size, float value) {
		int hs = size / 2;
		
		float a = getSample(x - hs, y);
		float b = getSample(x + hs, y);
		float c = getSample(x, y - hs);
		float d = getSample(x, y + hs);

		setSample(x, y, (a + b + c + d + value) / 5);
	}

}
