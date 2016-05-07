package luminoscore.graphics.loaders;

import java.awt.image.BufferedImage;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.models.RawModel;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * Class that loads objects to the graphics card
 *
 */

public class Loader {
	
	private ModelLoader modelLoader = new ModelLoader();
	private ImageLoader imageLoader = new ImageLoader();

	public RawModel loadToVAO(float[] positions, int dimensions) {
		return modelLoader.loadToVAO(positions, dimensions);
	}
	
	public int loadToVAO(float[] positions, float[] textureCoords) {
		return modelLoader.loadToVAO(positions, textureCoords);
	}
	
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
		return modelLoader.loadToVAO(vertices, textureCoords, indices);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, indices);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, List<Vector3f> vertices, int[] indices) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, indices);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int vertexCount) {
		return modelLoader.loadToVAO(positions, textureCoords, normals, vertexCount);
	}
	
	public int loadCubeMap(String[] textureFiles) {
		return imageLoader.loadCubeMap(textureFiles);
	}
	
	public int loadTexture(String fileName){
		return imageLoader.loadTexture(fileName);
	}
	
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

//**********************************Private Methods**********************************//

}
