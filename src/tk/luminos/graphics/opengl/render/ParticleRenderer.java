package tk.luminos.graphics.opengl.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.opengl.gameobjects.Camera;
import tk.luminos.graphics.opengl.loaders.Loader;
import tk.luminos.graphics.opengl.models.RawModel;
import tk.luminos.graphics.opengl.particles.Particle;
import tk.luminos.graphics.opengl.shaders.ParticleShader;
import tk.luminos.graphics.opengl.textures.ParticleTexture;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector3f;
import tk.luminos.tools.Maths;

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
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Renders particles to world
	 * 
	 * @param particles			Defines particles to render
	 * @param camera			Defines camera to get view matrix of
	 */
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		
		for(ParticleTexture texture : particles.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
			for(Particle particle : particles.get(texture)) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
				shader.loadTextureCoordInfo(particle.getOffsetOne(), particle.getOffsetTwo(), texture.getNumberOfRows(), particle.getBlend());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		finish();
	}
	
	/**
	 * Prepares particle for rendering
	 */
	public void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);
	}
	
	/**
	 * Finished rendering process
	 */
	public void finish() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
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
		shader.loadModelViewMatrix(modelViewMatrix);
	}

}
