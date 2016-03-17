package luminoscore.graphics.loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import luminoscore.graphics.models.RawModel;

public class VAOLoader {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Lists holding all VAO and VBO IDs
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();

	//Constructor
	public VAOLoader() {

	}
	
	//Cleans up the GPU.  After done with render loop.
	public void destroy() {
		for(Integer vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(Integer vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		vaos.clear();
		vbos.clear();
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param indices the integer array containing the positions of the indices
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, int[] indices, int dimensions) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, dimensions, positions);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param textures the float array containing texture coordinates
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, float textures[], int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		storeDataInAttributeList(1, 2, textures);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param textures the float array containing texture coordinates
	 * @param indices the integer array containing the positions of the indices
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, float textures[], int[] indices, int dimensions) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, dimensions, positions);
		storeDataInAttributeList(1, 2, textures);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param textures the float array containing texture coordinates
	 * @param normals the float array containing the normal vector
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	
	public RawModel loadToVAO(float[] positions, float textures[], float[] normals, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		storeDataInAttributeList(1, 2, textures);
		storeDataInAttributeList(2, dimensions, normals);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @param positions the float array containing vertices of a model
	 * @param textures the float array containing texture coordinates
	 * @param normals the float array containing the normal vector
	 * @param indices the integer array containing the positions of the indices
	 * @param dimensions contains the number of dimensions a model has
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, float textures[], float[] normals, int[] indices, int dimensions) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, dimensions, positions);
		storeDataInAttributeList(1, 2, textures);
		storeDataInAttributeList(2, dimensions, normals);
		return new RawModel(vaoID, positions.length);
	}
	
	/*
	 * @return int
	 * 
	 * Binds a VAO and gets it GLuint to an int
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);;
		return vaoID;
	}
	
	/*
	 * @param attributeNumber the VBO location at which the data is to be stored
	 * @param coordiniateSize the number of dimensions the data is stored in
	 * @param data the data to be stored in the VBO
	 * 
	 * Binds data to a VAO via a VBO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/*
	 * @param indices the array of indices to be bound
	 * 
	 * Binds the indices to an element array buffer
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/*
	 * @param data the data to be sent to the buffer
	 * @return IntBuffer
	 * 
	 * Stores an array of Integers into an IntBuffer
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/*
	 * @param data the data to be sent to the buffer
	 * @return FloatBuffer
	 * 
	 * Stores an array of Floats into a FloatBuffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
