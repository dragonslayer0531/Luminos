package tk.luminos.graphics.render;

import tk.luminos.Application;
import tk.luminos.graphics.FrameBufferObject;
import tk.luminos.graphics.shaders.ShaderProgram;

public abstract class PostProcessor {
	
	protected ShaderProgram shader;
	protected ImageRenderer renderer;
	
	public PostProcessor(ShaderProgram shader) {
		this.shader = shader;
		renderer = new ImageRenderer(new FrameBufferObject(Application.getValue("WIDTH"), Application.getValue("HEIGHT"), false));
	}
	
	public abstract void start();
	public abstract void stop();
	public abstract void render();

}
