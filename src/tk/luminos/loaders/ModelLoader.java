package tk.luminos.loaders;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.maths.Matrix4;

/**
 * 
 * Loads model data to GPU
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class ModelLoader {
	
	protected ModelLoader() {}
	
	/**
	 * Loads Positions to graphics card
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param dimensions	int used to describe the number of dimensions
	 * @return				RawModel containing GPU data on the vertices
	 */
	protected RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	/**
	 * Loads positions to graphics card
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param elements		integers used to describe the order of the vertices
	 * @param dimensions	int used to describe the number of dimensions
	 * @return				RawModel containing GPU data on the vertices
	 */
	protected RawModel loadToVAO(float[] positions, int[] elements, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, 3, positions);
		this.bindIndicesBuffer(elements);
		this.unbindVAO();
		return new RawModel(vaoID, positions.length / 3);
	}
	
	/**
	 * Loads positions and texture coordinates to the graphics card
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @return 				integer used to describe the index of the data on the GPU
	 */
	protected int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	/**
	 * Loads Positions, Texture Coordinates, and Indices to the graphics card
	 * 
	 * @param vertices		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param indices		integers used to describe the order of the vertices
	 * @return 				RawModel containing GPU data on the vertices
	 */
	protected RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		bindIndicesBuffer(indices);
		unbindVAO();
		return new RawModel(vaoID, vertices.length / 3);
	}
	
	/**
	 * Loads Positions, Texture Coordinates, Normals, and Indices to graphics card.  3D Coordinates.
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param indices		integers used to describe the order of vertices
	 * @return 				RawModel containing GPU data on the vertices.
	 */
	protected RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		bindIndicesBuffer(indices);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Loads Positions, Texture Coordinates, Normals, and Vertex Count to graphics card.  3D Coordinates.
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param vertexCount	integer used to describe total number of vertices
	 * @return 				RawModel containing GPU data on the vertices.
	 */
	protected RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int vertexCount) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, vertexCount);
	}
	
	/**
	 * Loads Positions, Texture Coordinates, Normals, and Vertex Count to graphics card.  3D Coordinates.
	 * 
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param tangents		floats used to describe the tangential normals of the data
	 * @param indices		integer array used to describe the order of the vertices
	 * @return 				RawModel containing GPU data on the vertices.
	 */
	protected RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, tangents);
		bindIndicesBuffer(indices);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	protected VertexArray load(float positions[], int dimensions) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, dimensions);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, int[] indices, int dimensions) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, dimensions);
		vao.createIndexBuffer(indices);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float positions[], float textureCoords[], int dimensions) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, dimensions);
		vao.createAttribute(1, textureCoords, 2);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float positions[], float textureCoords[], int[] indices, int dimensions) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, dimensions);
		vao.createAttribute(1, textureCoords, 2);
		vao.createIndexBuffer(indices);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, float[] normals) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.createIndexBuffer(indices);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, float[] normals, float[] tangents) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.createAttribute(3, tangents, 3);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.createAttribute(3, tangents, 3);
		vao.createIndexBuffer(indices);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, int[] indices) {
		VertexArray vao = new VertexArray();
		vao.start();
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createIndexBuffer(indices);
		vao.stop();
		return vao;
	}
	
	protected VertexArray load(float[] positions, float[] textureCoords, float[] normals, Matrix4[] transformations, int[] indices) {
		return null;
	}
	
	/**
	 * @return int	integer describing the index of a vertex array
	 * 
	 * Creates blank vertex array on graphics card
	 */
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		Loader.vaos.add(vaoID);
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
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		Loader.vbos.add(vboID);
	}
	
	/**
	 * @param indices	integers describing the order of the vertices
	 * 
	 * Binds an element array buffer to the vertex array (VAO)
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		Loader.vbos.add(vboID);
	}
	
	/**
	 * Unbinds Vertex Array
	 */
	private void unbindVAO() {
		glBindVertexArray(0);
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
