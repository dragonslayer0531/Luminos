package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;
import java.util.Map;

import tk.luminos.graphics.Camera;
import tk.luminos.graphics.Particle;
import tk.luminos.graphics.ParticleTexture;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.ParticleShader;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector2f;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Renders particle effects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;
	
	/**
	 * Constructor
	 * 
	 * @param shader			Defines shader to render with
	 * @param loader			Defines loader to use
	 * @param projectionMatrix	Defines projection matrix
	 */
	public ParticleRenderer(ParticleShader shader, Loader loader, Matrix4f projectionMatrix) {
		quad = loader.loadToVAO(VERTICES, 2);
		this.shader = shader;
		shader.start();
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Renders particles to world
	 * 
	 * @param particles			Defines particles to render
	 * @param camera			Defines camera to get view matrix of
	 */
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera){
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		prepare();
		
		for(ParticleTexture texture : particles.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getID());
			for(Particle particle : particles.get(texture)) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
				shader.setUniform("texOffset1", particle.getOffsetOne());
				shader.setUniform("texOffset2", particle.getOffsetTwo());
				shader.setUniform("texCoordInfo", new Vector2f(texture.getNumberOfRows(), particle.getBlend()));
				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		finish();
	}
	
	/**
	 * Prepares particle for rendering
	 */
	public void prepare() {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glDepthMask(false);
	}
	
	/**
	 * Finished rendering process
	 */
	public void finish() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}
	
	/**
	 * Cleans up shader
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
	
//*******************************Private Methods*****************************************//
	
	/**
	 * Creates MV Matrix for particle
	 * 
	 * @param position		Position of particle
	 * @param rotation		Rotation of particle
	 * @param scale			Scale of particle
	 * @param viewMatrix	View matrix of camera
	 */
	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(position, modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		shader.setUniform("modelViewMatrix", modelViewMatrix);
	}

}
