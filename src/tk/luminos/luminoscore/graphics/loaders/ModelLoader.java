package tk.luminos.luminoscore.graphics.loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.luminoscore.graphics.models.Mesh;
import tk.luminos.luminoscore.graphics.models.RawModel;

/**
 * 
 * Loads model data to GPU
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class ModelLoader {
	

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	protected ModelLoader() {}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param dimensions	int used to describe the number of dimensions [1, 4]
	 * @return	RawModel	RawModel containing GPU data on the vertices
	 * 
	 * Loads Positions to graphics card
	 */
	protected RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @return int			integer used to describe the index of the data on the GPU
	 * 
	 * Loads positions and texture coordinates to the graphics card
	 */
	protected int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	/**
	 * @param vertices		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param indices		integers used to describe the order of the vertices
	 * @return RawModel		RawModel containing GPU data on the vertices
	 * 
	 * Loads Positions, Texture Coordinates, and Indices to the graphics card
	 */
	protected RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, vertices.length / 3);
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param indices		integers used to describe the order of vertices
	 * @return RawModel		RawModel containing GPU data on the vertices.
	 * 
	 * Loads Positions, Texture Coordinates, Normals, and Indices to graphics card.  3D Coordinates.
	 */
	protected RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length, new Mesh(positions, normals));
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param vertexCount	integer used to describe total number of vertices
	 * @return RawModel		RawModel containing GPU data on the vertices.
	 * 
	 * Loads Positions, Texture Coordinates, Normals, and Vertex Count to graphics card.  3D Coordinates.
	 */
	protected RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int vertexCount) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, vertexCount);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, String id) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		RawModel rm = new RawModel(vaoID, indices.length, new Mesh(positions, normals));
		return rm;
	}
	
	/**
	 * Cleans up ModelLoader
	 */
	
	protected void cleanUp() {
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	/**
	 * @return int	integer describing the index of a vertex array
	 * 
	 * Creates blank vertex array on graphics card
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * @param attributeNumber		integer describing where to store the data
	 * @param coordinateSize		integer describing the dimensions of the data [1, 4]
	 * @param data					floats describing the data to be put to the attribute
	 * 
	 * Stores float array in attribute of VAO
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
	
	/**
	 * @param indices	integers describing the order of the vertices
	 * 
	 * Binds an element array buffer to the vertex array (VAO)
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Unbinds Vertex Array
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * @param data			integers to be stored in an intbuffer
	 * @return	IntBuffer	IntBuffer of original data
	 * 
	 * Converts int array to intbuffer
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * @param data 			floats to be stored in a floatbuffer
	 * @return	FloatBuffer	FloatBuffer of original data
	 * 
	 * Converts float arry to floatbuffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
