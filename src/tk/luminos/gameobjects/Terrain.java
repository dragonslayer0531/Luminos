package tk.luminos.gameobjects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import tk.luminos.Application;
import tk.luminos.Debug;
import tk.luminos.graphics.ProceduralTerrainTexture;
import tk.luminos.graphics.TerrainTexture;
import tk.luminos.graphics.TerrainTexturePack;
import tk.luminos.graphics.VertexArray;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Vector;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.util.FractalNoise;
/**
 * 
 * Terrain class
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class Terrain {

	public static int VERTEX_COUNT = 32;
	private static float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private static Integer SIZE = Application.getValue("SIZE");

	private float x;
	private float z;
	private VertexArray model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private boolean renderable = true;
	private FractalNoise noise;

	private float[][] heights;

	private float[] normals;

	/**
	 * Constructor
	 * 
	 * @param gridX			Terrain Grid X Position
	 * @param gridZ			Terrain Grid Z Position
	 * @param seed			PerlinNoise seed
	 * @param texturePack	Texture Pack to use
	 */
	public Terrain(float gridX, float gridZ, int seed, TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.noise = new FractalNoise((int) gridX, (int) gridZ, VERTEX_COUNT, seed, TerrainType.Type.HILLS);
		this.model = generateTerrain(noise);
		this.blendMap = new TerrainTexture(Loader.getInstance().loadTexture(ProceduralTerrainTexture.generateTerrainMap(this)));
	}

	/**
	 * Constructor
	 * 
	 * @param gridX			Terrain Grid X Position
	 * @param gridZ			Terrain Grid Z Position
	 * @param texturePack	Texture Pack to use
	 * @param blendMap		Blend Map for textures
	 * @param heightMap		Height map to use
	 */
	public Terrain(float gridX, float gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(heightMap);
	}
	
	/**
	 * Constructor
	 * 
	 * @param model			Vertex array describing terrain
	 * @param heights		2 dimensional array describing height values
	 * @param textures		2 dimensional array describing texture coordinates
	 * @param blendMap		BufferedImage to use as blend map
	 * @param loader		Loader to use
	 * @param gridX			Grid X coordinate
	 * @param gridZ			Grid Z coordinate
	 * @throws Exception	Exception for if file isn't found or cannot be handled
	 */
	public Terrain(VertexArray model, float[][] heights, List<String> textures, BufferedImage blendMap, Loader loader, float gridX, float gridZ) throws Exception {
		this.model = model;
		this.heights = heights;
		this.texturePack = new TerrainTexturePack(new TerrainTexture(loader.loadTexture(textures.get(0))), 
				new TerrainTexture(loader.loadTexture(textures.get(1))), 
				new TerrainTexture(loader.loadTexture(textures.get(2))), 
				new TerrainTexture(loader.loadTexture(textures.get(3))));
		this.blendMap = new TerrainTexture(loader.loadTexture(blendMap));
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
	}
	
	/**
	 * Constructor
	 * 
	 * @param model			VertexArray describing terrain
	 * @param heights		2 dimensional array describing height values
	 * @param textures		Texture Pack
	 * @param blendMap		BufferedImage to use as blend map
	 * @param x				X coordinate of terrain
	 * @param z				Z coordinate of terrain
	 */
	public Terrain(VertexArray model, float[][] heights, TerrainTexturePack textures, TerrainTexture blendMap, float x, float z) {
		this.model = model;
		this.heights = heights;
		this.texturePack = textures;
		this.blendMap = blendMap;
		this.x = x;
		this.z = z;
	}
		
	/**
	 * Calculates whether the location is within a terrain's bounds
	 * 
	 * @param pos		Position to calculate with
	 * @return 			Inside terrain bounds
	 */
	public boolean isOnTerrain(Vector3 pos) {
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
	 * Calculates whether the entity is within a terrain's bounds
	 * 
	 * @param entity	GameObject to calculate with
	 * @return 			Inside terrain bounds
	 */
	public boolean isOnTerrain(GameObject entity) {
		return isOnTerrain((Vector3) entity.getPosition());
	}

	/**
	 * Get world x coordinate of terrain
	 * 
	 * @return x value of terrain
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get world z coordinate of terrain
	 * 
	 * @return z value of terrain
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Get terrain's model
	 * 
	 * @return vertex array describing terrain
	 */
	public VertexArray getVertexArray() {
		return model;
	}

	/**
	 * Gets the terrain's texture pack
	 * 
	 * @return texture pack used by terrain
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * Gets blend map texture
	 * 
	 * @return blend map used by terrain
	 */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	/**
	 * Gets height of terrain
	 * 
	 * @param worldX	World X Coordinate
	 * @param worldZ	World Z Coordinate
	 * @return 			Height of terrain at given point
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
			answer = MathUtils.barryCentric(new Vector3(0, heights[gridX][gridZ], 0), new Vector3(1,
					heights[gridX + 1][gridZ], 0), new Vector3(0,
							heights[gridX][gridZ + 1], 1), new Vector2(xCoord, zCoord));
		} else {
			answer = MathUtils.barryCentric(new Vector3(1, heights[gridX + 1][gridZ], 0), new Vector3(1,
					heights[gridX + 1][gridZ + 1], 1), new Vector3(0,
							heights[gridX][gridZ + 1], 1), new Vector2(xCoord, zCoord));
		}

		return answer;
	}
	
	private float[] vertices;
	private float[] textureCoords;
	private int[] indices;

	/**
	 * Generates raw model of terrain
	 * 
		 * @param noise		Fractal Noise to be used in terrain height generation
	 * @return 			Model of terrain
	 */
	private VertexArray generateTerrain(FractalNoise noise) {
		int count = (int) Math.pow(VERTEX_COUNT, 2);
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textureCoords = new float[count * 2];
		indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, noise);
				vertices[vertexPointer * 3 + 1] = height;
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3 normal = calculateNormal(j, i, noise);
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
		
		return Loader.getInstance().load(vertices, textureCoords, normals, indices);
	}

	/**
	 * Creates model of terrain
	 * 
	 * @param heightMap		Heightmap file to be used
	 * @return 				Model describing the terrain
	 */
	private VertexArray generateTerrain(String heightMap) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/textures/" + heightMap + ".png"));
		} catch (IOException e) {
			Debug.addData(e);
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
				Vector3 normal = calculateNormal(j, i, image);
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
		return Loader.getInstance().load(vertices, textureCoords, normals, indices);
	}

	/**
	 * Gets scale of terrain
	 * 
	 * @return Scale of terrain
	 */
	public float getScale() {
		return SIZE;
	}
	
	/**
	 * Generates height value
	 * 
	 * @param x		X value of height
	 * @param z		Z value of height
	 * @param noise	FractalNoise to use in generation
	 * @return		Height value of given x, z
	 */
	public float getHeight(int x, int z, FractalNoise noise) {
		return noise.generateHeight(x, z);
	}
	
	/**
	 * Gets the normals of the terrain
	 * 
	 * @return	Normal vectors of terrain
	 */
	public float[] getNormals() {
		return normals;
	}
	
	/**
	 * Gets the height array of the terrain
	 * 
	 * @return 2 dimensional array of heights
	 */
	public float[][] getHeights() {
		return heights;
	}
	
	public Vector getPosition() {
		return new Vector3(x, 0, z);
	}
	
	public void increasePosition(Vector delta) {
		Vector2 increase = (Vector2) delta;
		this.x += increase.x;
		this.z += increase.y;
	}
	
	public void setRenderable(boolean renderable) {
		this.renderable = renderable;
	}
	
	public boolean isRenderable() {
		return renderable;
	}
	
	public FractalNoise getNoise() {
		return noise;
	}


//**************************************************Private Methods*********************************************//
	
	/**
	 * Calculates normal vector
	 * 
	 * @param x		    X coordinate
	 * @param z		    Z coordinate
	 * @param image 	Image to use
	 * @return Normal vector
	 */
	private Vector3 calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		Vector3 normal = new Vector3(heightL-heightR, 2f, heightD - heightU);
		normal.normalize();
		return normal;
	}

	/**
	 * Calculates normal vector
	 * 
	 * @param x			X coordinate
	 * @param z			Z coordinate
	 * @param noise		Noise to use
	 * @return Normal vector
	 * 
	 */
	private Vector3 calculateNormal(int x, int z, FractalNoise noise) {
		float heightL = getHeight(x - 1, z, noise);
		float heightR = getHeight(x + 1, z, noise);
		float heightD = getHeight(x, z - 1, noise);
		float heightU = getHeight(x, z + 1, noise);
		Vector3 normal = new Vector3(heightL - heightR, 2f, heightD - heightU);
		normal.normalize();
		return normal; 
	}

	/**
	 * Calculates height of pixel
	 * 
	 * @param x			X coordinate
	 * @param z			Z coordinate
	 * @param image		Image to use
	 * @return 			Height at pixel
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
