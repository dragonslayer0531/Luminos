package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;
import java.util.Map;

import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.shaders.ShadowShader;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.tools.Maths;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * Renders shadow maps for entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * Constructor
	 * 
	 * @param shader				Defines shader to use
	 * @param projectionViewMatrix	Defines projectionView matrix for rendering
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders shadow map to buffer
	 * 
	 * @param entities	Defines entities to render to shadow map
	 */
	protected void render(Map<TexturedModel, List<GameObject>> entities, List<Terrain> terrains) {
		for (TexturedModel model : entities.keySet()) {
			RawModel rawModel = model.getRawModel();
			bindModel(rawModel);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, model.getMaterial().getDiffuseID());
			for (GameObject entity : entities.get(model)) {
				if (!entity.isRenderable())
					continue;
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(),
						GL_UNSIGNED_INT, 0);
			}
		}
		for (Terrain terrain : terrains) {
			RawModel model = terrain.getRawModel();
			glBindTexture(GL_TEXTURE_2D, terrain.getTexturePack().getBackgroundTexture().getID());
			bindModel(model);
			prepareInstance(terrain);
			glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		}
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

//*************************************Private Methods********************************//
	
	/**
	 * Binds raw model to GPU
	 * 
	 * @param rawModel		RawModel to be bound
	 */
	private void bindModel(RawModel rawModel) {
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}

	/**
	 * Prepares entity to be rendered
	 * 
	 * @param entity		Entity to be prepared
	 */
	private void prepareInstance(GameObject entity) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix((Vector3f) entity.getPosition(),
				entity.getRotation(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}
	
	private void prepareInstance(Terrain terrain) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix((Vector3f) terrain.getPosition(), new Vector3f(0, 0, 0),
				1);
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}

}
