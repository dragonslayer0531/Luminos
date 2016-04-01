package luminoscore.graphics.terrains;

import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.terrains.TerrainType.Type;

public class TerrainBuilder extends Thread {

	public float x;
	public float z;
	public int vertex_count, seed;
	public Type terrain_type = Type.PLAINS;
	
	public static int size = 800;

	public float[] vertices, normals, textures;
	public float[][] heights;
	public int[] indices;
	public boolean complete = false;
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public int getVertex_count() {
		return vertex_count;
	}

	public int getSeed() {
		return seed;
	}

	public Type getTerrain_type() {
		return terrain_type;
	}

	public static int getSize() {
		return size;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTextures() {
		return textures;
	}

	public float[][] getHeights() {
		return heights;
	}

	public int[] getIndices() {
		return indices;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public TerrainBuilder(float x, float z, int vertex_count, int seed, Type terrain_type) {
		this.x = x;
		this.z = z;
		this.vertex_count = vertex_count;
		this.seed = seed;
		this.terrain_type = terrain_type;
	}
	
	public void run() {
		PerlinNoise noise = new PerlinNoise((int) x, (int) z, vertex_count, seed, terrain_type);
		int count = (int) Math.pow(vertex_count, 2);
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textures = new float[count * 2];
		indices = new int[6 * (vertex_count - 1) * (vertex_count * 1)];
		int vertexPointer = 0;
		heights = new float[vertex_count][vertex_count];
		for(int i = 0; i < vertex_count; i++) {
			for(int j = 0; j < vertex_count; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) vertex_count - 1) * size;
				float height = getHeight(j, i, noise);				
				vertices[vertexPointer * 3 + 1] = height;
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertex_count - 1) * size;
				Vector3f normal = calculateNormal(j, i, noise);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textures[vertexPointer * 2] = (float) j / ((float) vertex_count - 1);
				textures[vertexPointer * 2 + 1] = (float) i / ((float) vertex_count - 1);
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for(int gz = 0; gz < vertex_count - 1; gz++) {
			for(int gx = 0; gx < vertex_count - 1; gx++) {
				int topLeft = (gz * vertex_count) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertex_count) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		this.complete = true;
		
	}
	
	public static float getHeight(int x, int z, PerlinNoise noise) {
		return noise.generateHeight(x, z);
	}
	
	private Vector3f calculateNormal(int x, int z, PerlinNoise noise) {
		float heightL = getHeight(x - 1, z, noise);
		float heightR = getHeight(x + 1, z, noise);
		float heightD = getHeight(x, z - 1, noise);
		float heightU = getHeight(x, z + 1, noise);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		return normal.normalise(normal);
	}
	
}
