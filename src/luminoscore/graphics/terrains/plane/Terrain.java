package luminoscore.graphics.terrains.plane;

import luminoscore.graphics.loaders.VAOLoader;
import luminoscore.graphics.models.RawModel;
import luminoscore.util.math.Vector3f;

public class Terrain {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/17/2016
	 */
	
	//Terrain Static Data
	public static int SIZE = 100;
	public static int VERTEX_COUNT = 32;
	public static int heightCeiling;
	
	//Object Values
	private float[][] heights;
	private float[] normals;
	private RawModel model;
	
	//Constructor Values
	private float x, z;
	private float seed;

	/*
	 * @param gridX Defines the grid location on the X axis
	 * @param gridZ Defines the grid location on the Z axis
	 * @param seed Defines the seed for the fractal noise
	 * @param loader Defines the VAOLoader to load objects to the GPU
	 * 
	 * Constructor
	 */
	public Terrain(float gridX, float gridZ, int seed, VAOLoader loader) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, new FractalNoise((int) gridX, (int) gridZ, VERTEX_COUNT, seed));
	}
	
	/*
	 * @param loader Defines the VAOLoader to use
	 * @param noise Defines the Fractal Noise to use
	 * @return RawModel
	 * 
	 * Generates RawModel based off of a fractal noise
	 */
	private RawModel generateTerrain(VAOLoader loader, FractalNoise noise) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3]; //Total number of Vectors * 3 components each
		normals = new float[count * 3]; //Total number of Vectors * 3 components each
		float[] textureCoords = new float[count * 2];  //Total number of Vectors * 2 components each
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) for(int j = 0; j < VERTEX_COUNT; j++) {
			vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
			float height = getHeight(j, i, noise);
			vertices[vertexPointer * 3 + 1] = height;
			heights[j][i] = height;
			vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
			Vector3f normal = calculateNormal(j, i, noise);
			normals[vertexPointer * 3] = normal.x;
			normals[vertexPointer * 3 + 1] = normal.y;
			normals[vertexPointer * 3 + 2] = normal.z;
			textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
			textureCoords[vertexPointer * 2 + 1] = (float) j / ((float) VERTEX_COUNT - 1);
			vertexPointer++;
		}
		
		int pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) for(int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
			int topLeft = (gz * VERTEX_COUNT) + gx;
			int topRight = topLeft + 1;
			int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
			int bottomRight = bottomLeft + 1;
			indices[pointer++] = topLeft;
			indices[pointer++] = bottomLeft;
			indices[pointer++] = topRight;
			indices[pointer++] = topRight;
			indices[pointer++] = bottomLeft;
			indices[pointer++] = bottomRight;
 		}
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices, 3);
	}
	
	/*
	 * @param x X coordinate to return noise value of
	 * @param z Z coordinate to return noise value of
	 * @param noise Noise Generator 
	 * @return float
	 * 
	 * Gets the height value
	 */
	private float getHeight(int x, int z, FractalNoise noise) {
		return noise.generateHeight(x, z);
	}
	
	/*
	 * @param x X coordinate to find normal at
	 * @param z Z coordinate to find normal at
	 * @param noise Fractal Noise to calculate with
	 * @return Vector3f
	 * 
	 * Calculates and returns normal of point
	 */
	private Vector3f calculateNormal(int x, int z, FractalNoise noise) {
		float heightL = getHeight(x - 1, z, noise);
		float heightR = getHeight(x + 1, z, noise);
		float heightD = getHeight(x, z - 1, noise);
		float heightU = getHeight(x, z + 1, noise);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		return normal.normalise();
	}

	//Getter-Setter Methods
	public static int getHeightCeiling() {
		return heightCeiling;
	}

	public static void setHeightCeiling(int heightCeiling) {
		Terrain.heightCeiling = heightCeiling;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float[][] getHeights() {
		return heights;
	}

	public RawModel getModel() {
		return model;
	}

	public float getSeed() {
		return seed;
	}

}
