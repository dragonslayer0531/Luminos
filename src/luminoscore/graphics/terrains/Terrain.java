package luminoscore.graphics.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.Debug;
import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.textures.TerrainTexture;
import luminoscore.graphics.textures.TerrainTexturePack;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Terrain class
 *
 */

public class Terrain {

	public static float SIZE = 100;
	public static int VERTEX_COUNT = 32;
	private static float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;

	private float[][] heights;

	private float[] normals;

	/**
	 * @param gridX			Terrain Grid X Position
	 * @param gridZ			Terrain Grid Z Position
	 * @param seed			PerlinNoise seed
	 * @param loader		Loader to use
	 * @param blendMap		Blend Map for textures
	 * @param texturePack	Texture Pack to use
	 */
	public Terrain(float gridX, float gridZ, int seed, Loader loader, TerrainTexture blendMap, TerrainTexturePack texturePack) {

		this.texturePack = texturePack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.blendMap = blendMap;
		this.model = generateTerrain(loader, new PerlinNoise((int) gridX, (int) gridZ, VERTEX_COUNT, seed, TerrainType.Type.HILLS));

	}

	/**
	 * @param gridX			Terrain Grid X Position
	 * @param gridZ			Terrain Grid Z Position
	 * @param loader		Loader to use
	 * @param texturePack	Texture Pack to use
	 * @param blendMap		Blendd Map for textures
	 * @param heightMap		Height map to use
	 */
	public Terrain(float gridX, float gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}

	/**
	 * @param pos		Position to calculate with
	 * @return boolean	Inside terrain bounds
	 * 
	 * Calculates whether the location is within a terrain's bounds
	 */
	public boolean isOnTerrain(Vector3f pos) {
		float xMin = this.getX();
		float xMax = this.getX() + this.getScale();
		float zMin = this.getZ();
		float zMax = this.getZ() + this.getScale();				
		if(pos.x >= xMin && pos.x < xMax && pos.z >= zMin && pos.z < zMax) {
			return true;
		}
		return false;
	}

	/**
	 * @param entity	Entity to calculate with
	 * @return boolean	Inside terrain bounds
	 * 
	 * Calculates whether the entity is within a terrain's bounds
	 */
	public boolean isOnTerrain(Entity entity) {
		return isOnTerrain(entity.getPosition());
	}

	/**
	 * @return float X
	 * 
	 * Get world x coordinate of terrain
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return float Z
	 * 
	 * Get world z coordinate of terrain
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @return RawModel	model of terrain
	 * 
	 * Get terrain's model
	 */
	public RawModel getModel() {
		return model;
	}

	/**
	 * @return TerrainTexturePack	texture pack used by terrain
	 * 
	 * Gets the terrain's texture pack
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * @return TerrainTexture	blend map used by terrain
	 * 
	 * Gets blend map texture
	 */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	/**
	 * @param worldX	World X Coordinate
	 * @param worldZ	World Z Coordinate
	 * @return float	Height of terrain at given point
	 * 
	 * Gets height of terrain
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		if(gridX < 0 && gridZ < 0) return heights[0][0];
		if(gridX >= heights.length - 1) return heights[gridX][gridZ];
		else if(gridX < 0) return heights[0][gridZ];
		else if(gridZ >= heights.length - 1) return heights[gridX][gridZ];
		else if(gridZ < 0) return heights[gridX][0];

		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float answer;

		if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	/**
	 * @param loader	Loader to be used
	 * @param noise		Fractal Noise to be used in terrain height generation
	 * @return RawModel	Model of terrian
	 * 
	 * Generates raw model of terrain
	 */
	private RawModel generateTerrain(Loader loader, PerlinNoise noise) {
		int count = (int) Math.pow(VERTEX_COUNT, 2);
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
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
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;

			}

		}

		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
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
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/**
	 * @param loader		Loader to be used
	 * @param heightMap		Heightmap file to be used
	 * @return RawModel		Model of terrain
	 * 
	 * Creates model of terrain
	 */
	private RawModel generateTerrain(Loader loader, String heightMap) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/textures/" + heightMap + ".png"));
		} catch (IOException e) {
			Debug.addData(Terrain.class + " " + e.getMessage());
		}
		int VERTEX_COUNT = image.getHeight();
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				vertices[vertexPointer * 3 + 1] = height;
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
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
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/**
	 * @return float scale
	 * 
	 * Gets scale of terrain
	 */
	public float getScale() {
		return SIZE;
	}
	
	/**
	 * @param x
	 * @param z
	 * @param noise
	 * @return
	 */
	public float getHeight(int x, int z, PerlinNoise noise) {
		return noise.generateHeight(x, z);
	}
	
	public float[] getNormals() {
		return normals;
	}

//**************************************************Private Methods*********************************************//
	
	/**
	 * @param x		    X coordinate
	 * @param z		    Z coordinate
	 * @param image 	Image to use
	 * @return Vector3f	Normal
	 * 
	 * Calculates normal vector
	 */
	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	/**
	 * @param x			X coordinate
	 * @param z			Z coordinate
	 * @param noise		Noise to use
	 * @return Vector3f	Normal
	 * 
	 * Calculates normal vector
	 */
	private Vector3f calculateNormal(int x, int z, PerlinNoise noise) {
		float heightL = getHeight(x - 1, z, noise);
		float heightR = getHeight(x + 1, z, noise);
		float heightD = getHeight(x, z - 1, noise);
		float heightU = getHeight(x, z + 1, noise);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		return normal.normalise(normal);
	}

	/**
	 * @param x			X coordinate
	 * @param z			Z coordinate
	 * @param image		Image to use
	 * @return float	Height
	 * 
	 * Calculates height of pixel
	 */
	private float getHeight(int x, int z, BufferedImage image){
		if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()){
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;
	}

}
