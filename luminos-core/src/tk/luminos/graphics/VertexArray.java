package tk.luminos.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds information about vertex arrays
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class VertexArray {
	
	private static final int BYTES_PER_FLOAT = 4;
	private static final int BYTES_PER_INT   = 4;
	
	private final int id;
	private List<VertexBuffer> vbos = new ArrayList<VertexBuffer>();
	private Set<Integer> attributes = new HashSet<Integer>();
	private VertexBuffer index;
	private int indexCount;
	
	/**
	 * Generates a new vertex array on the GPU
	 */
	public VertexArray() {
		this.id = glGenVertexArrays();
	}
	
	/**
	 * Retrieves the index count of the vertex array
	 * 
	 * @return		index count
	 */
	public int getIndexCount() {
		return indexCount;
	}
	
	/**
	 * Binds vertex array for loading VBOs
	 */
	public void start() {
		glBindVertexArray(id);
	}
	
	/**
	 * Unbinds vertex array for loading VBOs
	 */
	public void stop() {
		glBindVertexArray(0);
	}
	
	/**
	 * Binds vertex array and relevant vertex attribute
	 * arrays
	 */
	public void bind() {
		glBindVertexArray(id);
		for (int i : attributes)
			glEnableVertexAttribArray(i);
	}
	
	/**
	 * Unbinds vertex array and relevant vertex attribute
	 * arrays
	 */
	public void unbind() {
		for (int i : attributes)
			glDisableVertexAttribArray(i);
		glBindVertexArray(0);
	}
	
	/**
	 * Creates and attaches index buffer to the vertex array. An index buffer stores the 
	 * order of vertices, thus removing duplicate vertices in the VRAM.
	 * 
	 * @param indices		index order
	 */
	public void createIndexBuffer(int[] indices) {
		this.index = new VertexBuffer(GL_ELEMENT_ARRAY_BUFFER);
		index.bind();
		index.storeData(indices);
		this.indexCount = indices.length;
	}
	
	/**
	 * Creates and attaches an attribute containing an array of floats.  It is stored at 
	 * the index supplied by the attribute parameter.
	 * 
	 * @param attribute		attribute index
	 * @param data			float array to be stored to VRAM
	 * @param size			component count of the data
	 */
	public void createAttribute(int attribute, float[] data, int size) {
		VertexBuffer vb = new VertexBuffer(GL_ARRAY_BUFFER);
		vb.bind();
		vb.storeData(data);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, size * BYTES_PER_FLOAT, 0);
		this.indexCount = data.length / size;
		vb.unbind();
		vbos.add(vb);
		attributes.add(attribute);
	}
	
	/**
	 * Creates and attaches an attribute containing an array of integers.  It is stored at 
	 * the index supplied by the attribute parameter.
	 * 
	 * @param attribute		attribute index
	 * @param data			integer array to be stored to VRAM
	 * @param size			component count of the data
	 */
	public void createAttribute(int attribute, int[] data, int size) {
		VertexBuffer vb = new VertexBuffer(GL_ARRAY_BUFFER);
		vb.bind();
		vb.storeData(data);
		glVertexAttribPointer(attribute, size, GL_INT, false, size * BYTES_PER_INT, 0);
		vb.unbind();
		vbos.add(vb);
		attributes.add(attribute);
	}
	
	public void createInstancedAttribute(int attribute, float[] data, int size, int stride) {
		VertexBuffer vb = new VertexBuffer(GL_ARRAY_BUFFER);
		vb.bind();
		vb.storeEmpty(data.length);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, size * BYTES_PER_FLOAT, stride * BYTES_PER_FLOAT);
		glVertexAttribDivisor(attribute, 1);
		vb.unbind();
		vbos.add(vb);
		attributes.add(attribute);
	}
	
	public void createInstancedAttribute(int attribute, int[] data, int size) {
		
	}
	
	/**
	 * Deletes the vertex array, as well as corresponding vertex buffers from the 
	 * VRAM.
	 */
	public void delete() {
		glDeleteVertexArrays(id);
		for (VertexBuffer vbo : vbos)
			vbo.delete();
		index.delete();
	}

}
