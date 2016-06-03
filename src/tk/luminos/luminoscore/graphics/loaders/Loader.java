package tk.luminos.luminoscore.graphics.loaders;

import java.awt.image.BufferedImage;

import tk.luminos.luminoscore.graphics.models.RawModel;

/**
 * 
 * Class that loads objects to the graphics card
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class Loader {
	
	private ModelLoader modelLoader = new ModelLoader();
	private ImageLoader imageLoader = new ImageLoader();

	/**
	 * Loads an array of positions to the graphics card
	 * 
	 * @param positions		Positions to be loaded
	 * @param dimensions	Number of dimensions the positions take
	 * @return				RawModel describing the positions loaded
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		return modelLoader.loadToVAO(positions, dimensions);
	}
	
	/**
	 * Loads an array of positions and texture coordinates to the graphics card
	 * 
	 * @param positions		Positions to be loaded
	 * @param textureCoords	Texture coordinates of the positions
	 * @return				RawModel describing the positions and texture coordinates loaded
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		return modelLoader.loadToVAO(positions, textureCoords);
	}
	
	/**
	 * Loads an array of positions, texture coordinates, and indices to the graphics card
	 * 
	 * @param positions		Positions to be loaded		
	 * @param textureCoords	Texture coordinates of the positions
	 * @param indices		Indices describing the order of the positions
	 * @return				RawModel describing the positions and texture coordinates loaded
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
		return modelLoader.loadToVAO(positions, textureCoords, indices);
	}
	
	/**
	 * Loads an array of positions, texture coordinates, normal coordinates, and inidices to the graphics card
	 * 
	 * @param positions		Positions to be loaded
	 * @param textureCoords	Texture coordinates of the positions
	 * @param normals		Normal coordinates of the positions
	 * @param indices		Indices describing the order of the positions
	 * @return				RawModel describing the positions, texture coordinates, and normal coordinates loaded
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, indices);
	}
	
	/**
	 * Loads an array of positions, texture coordinates, normal coordinates, and inidices to the graphics card
	 * 
	 * @param positions		Positions to be loaded
	 * @param textureCoords	Texture coordinates of the positions
	 * @param normals		Normal coordinates of the positions
	 * @param indices		Indices describing the order of the positions
	 * @param id			String describing the file location
	 * @return				RawModel describing the positions, texture coordinates, and normal coordinates loaded
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, String id) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, indices, id);
	}
	
	/**
	 * Loads an array of positions, texture coordinates, normal coordinates, and vertex count to the graphics card
	 * 
	 * @param positions		Positions to be loaded
	 * @param textureCoords	Texture coordinates of the positions
	 * @param normals		Normal coordinates of the positions
	 * @param vertexCount	Integer describing the number of vertices
	 * @return				RawModel describing the positions, texture coordinates, and normal coordinates loaded
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int vertexCount) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, vertexCount);
	}
	
	/**
	 * Loads array of texture files to a cube map
	 * 
	 * @param textureFiles	Array of strings describing the location of texture files
	 * @return				Integer describing the cube map's index on the GPU
	 */
	public int loadCubeMap(String[] textureFiles) {
		return imageLoader.loadCubeMap(textureFiles);
	}
	
	/**
	 * Loads texture file to a cube map
	 * 
	 * @param fileName	String describing the location of the texture file
	 * @return			Integer describing the texture's index on the GPU
	 */
	public int loadTexture(String fileName){
		return imageLoader.loadTexture(fileName);
	}
	
	/**
	 * Loads buffered image to the graphics card
	 * 
	 * @param bImage	BufferedImage containing the texture data
	 * @return			Integer describing the texture's index on the GPU
	 */
	public int loadTexture(BufferedImage bImage) {
		return imageLoader.loadTexture(bImage);
	}
	

	/**
	 * Deletes VAOs, Buffers, and Textures from GPU
	 */
	public void cleanUp() {
		modelLoader.cleanUp();
		imageLoader.cleanUp();
	}

}
