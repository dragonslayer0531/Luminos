package luminoscore.graphics.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.shaders.ShadowShader;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Renders shadow maps for entities
 *
 */

public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader				Defines shader to use
	 * @param projectionViewMatrix	Defines projectionView matrix for rendering
	 * 
	 * Constructor
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * @param entities	Defines entities to render to shadowo map
	 * 
	 * Renders shadow map to buffer
	 */
	protected void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			RawModel rawModel = model.getRawModel();
			bindModel(rawModel);
			for (Entity entity : entities.get(model)) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

//*************************************Private Methods********************************//
	
	/**
	 * @param rawModel		RawModel to be bound
	 * 
	 * Binds raw model to GPU
	 */
	private void bindModel(RawModel rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	/**
	 * @param entity		Entity to be prepared
	 * 
	 * Prepares entity to be rendered
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotation(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
