package luminoscore.graphics.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import luminoscore.graphics.entities.Entity;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.shaders.EntityShader;
import luminoscore.graphics.textures.ModelTexture;
import luminoscore.util.math.matrix.Matrix4f;
import luminoscore.util.math.matrix.MatrixCreator;

public class EntityRenderer {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 */
	
	//Constructor Field
	private EntityShader shader;
	
	/*
	 * @param shader Takes in the entity shader
	 * @param projectionMatrix Takes in the projection matrix
	 * 
	 * Constructor
	 */
	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/*
	 * param entities Defines a map of TexturedModels and Lists of Entities
	 * 
	 * Renders the entity to the screen
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	/*
	 * Prepares the textured model of the entity to be rendered
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getModelTexture();
		shader.loadNumberOfRows(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	/*
	 * @param entity Defines entity to be rendered
	 * 
	 * Prepares the entity to be rendered
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = MatrixCreator.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(0, 0);
	}
	
	/*
	 * Unbinds the textured model
	 */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

}
