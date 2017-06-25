package tk.luminos.graphics.ui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.shaders.ShaderProgram;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;

public class GUIObject {
	
	private int texture;
	private Vector2 scale;
	private Vector2 position;
	private int zindex;
	private Vector3 color;
	
	public GUIObject(int texture, Vector3 color, int zindex, Vector2 scale, Vector2 position) {
		this.texture = texture;
		this.zindex = zindex;
		this.scale = scale;
		this.position = position;
		this.color = color;
	}

	/**
	 * @return the texture
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * @return the scale
	 */
	public Vector2 getScale() {
		return scale;
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * @return the zindex
	 */
	public int getZIndex() {
		return zindex;
	}
	
	public void render(ShaderProgram shader, VertexArray quad) {
		shader.start();
		quad.bind();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, this.getTexture());
		Matrix4 model = MathUtils.createTransformationMatrix(this.getPosition(), this.getScale());
		shader.setUniform("transformationMatrix", model);
		shader.setUniform("color", color);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getIndexCount());
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
		
		quad.unbind();
		shader.stop();
	}

}
