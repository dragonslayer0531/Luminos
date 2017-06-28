package tk.luminos.graphics;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

class VertexBuffer {
	
	private final int id;
	private final int type;
	
	public VertexBuffer(int type) {
		this.id = glGenBuffers();
		this.type = type;
	}
	
	public void bind() {
		glBindBuffer(type, id);
	}
	
	public void unbind() {
		glBindBuffer(type, 0);
	}
	
	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}

	public void storeData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	public void storeEmpty(int count) {
		glBufferData(type, count * 4, GL_STREAM_DRAW);
	}
	
	public void storeData(IntBuffer data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}
	
	public void storeData(FloatBuffer data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	public void delete() {
		glDeleteBuffers(id);
	}

}
