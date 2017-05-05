package tk.luminos.graphics.render;

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

import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.shaders.ShadowShader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

/**
 * 
 * Renders shadow maps for entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowMapEntityRenderer {

	private Matrix4 projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * Constructor
	 * 
	 * @param shader				Defines shader to use
	 * @param projectionViewMatrix	Defines projectionView matrix for rendering
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4 projectionViewMatrix) {
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
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
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
		Matrix4 modelMatrix = MathUtils.createTransformationMatrix((Vector3) entity.getPosition(),
				entity.getRotation(), entity.getScale());
		Matrix4 mvpMatrix = Matrix4.mul(projectionViewMatrix, modelMatrix, null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}
	
	private void prepareInstance(Terrain terrain) {
		Matrix4 modelMatrix = MathUtils.createTransformationMatrix((Vector3) terrain.getPosition(), new Vector3(0, 0, 0),
				1);
		Matrix4 mvpMatrix = Matrix4.mul(projectionViewMatrix, modelMatrix, null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}

}
