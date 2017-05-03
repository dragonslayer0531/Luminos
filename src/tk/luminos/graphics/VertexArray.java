package tk.luminos.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

public class VertexArray {
	
	private static final int BYTES_PER_FLOAT = 4;
	private static final int BYTES_PER_INT   = 4;
	
	public final int id;
	private List<VertexBuffer> vbos = new ArrayList<VertexBuffer>();
	private VertexBuffer index;
	private int indexCount;
	
	public VertexArray() {
		this.id = glGenVertexArrays();
	}
	
	public int getIndexCount() {
		return indexCount;
	}
	
	public void bind(int... attributes) {
		bind();
		for (int i : attributes)
			glEnableVertexAttribArray(i);
	}
	
	public void unbind(int... attributes) {
		for (int i : attributes)
			glDisableVertexAttribArray(i);
		unbind();
	}
	
	public void createIndexBuffer(int[] indices) {
		this.index = new VertexBuffer(GL_ELEMENT_ARRAY_BUFFER);
		index.bind();
		index.storeData(indices);
		this.indexCount = indices.length;
	}
	
	public void createAttribute(int attribute, float[] data, int size) {
		VertexBuffer vb = new VertexBuffer(GL_ARRAY_BUFFER);
		vb.bind();
		vb.storeData(data);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, size * BYTES_PER_FLOAT, 0);
		vb.unbind();
		vbos.add(vb);
	}
	
	public void createAttribute(int attribute, int[] data, int size) {
		VertexBuffer vb = new VertexBuffer(GL_ARRAY_BUFFER);
		vb.bind();
		vb.storeData(data);
		glVertexAttribPointer(attribute, size, GL_INT, false, size * BYTES_PER_INT, 0);
		vb.unbind();
		vbos.add(vb);
	}
	
	public void delete() {
		glDeleteVertexArrays(id);
		for (VertexBuffer vbo : vbos)
			vbo.delete();
		index.delete();
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}

}
