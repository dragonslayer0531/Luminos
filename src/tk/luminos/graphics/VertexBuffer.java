package tk.luminos.graphics;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VertexBuffer {
	
	private final int id;
	private final int type;
	
	public VertexBuffer(int type){
		this.id = glGenBuffers();
		this.type = type;
	}
	
	public void bind(){
		glBindBuffer(type, id);
	}
	
	public void unbind(){
		glBindBuffer(type, 0);
	}
	
	public void storeData(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}

	public void storeData(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	public void storeData(IntBuffer data){
		glBufferData(type, data, GL_STATIC_DRAW);
	}
	
	public void storeData(FloatBuffer data){
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	public void delete(){
		glDeleteBuffers(id);
	}

}
