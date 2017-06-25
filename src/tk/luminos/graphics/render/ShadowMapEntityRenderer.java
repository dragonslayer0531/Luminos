package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.List;
import java.util.Map;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.VertexArray;
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
	protected ShadowShader shader;

	/**
	 * Constructor
	 * 
	 * @param projectionViewMatrix	Defines projectionView matrix for rendering
	 * @throws Exception 			Thrown if shader cannot be loaded
	 */
	protected ShadowMapEntityRenderer(Matrix4 projectionViewMatrix) throws Exception {
		this.shader = new ShadowShader();
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders shadow map to buffer
	 * 
	 * @param entities	Defines entities to render to shadow map
	 */
	protected void render(Map<TexturedModel, List<GameObject>> entities, List<Terrain> terrains) {
		for (TexturedModel model : entities.keySet()) {
			VertexArray rawModel = model.getVertexArray();
			rawModel.bind();
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, model.getMaterial().getTexture().getId());
			for (GameObject entity : entities.get(model)) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, rawModel.getIndexCount(), GL_UNSIGNED_INT, 0);
			}
			rawModel.unbind();
		}
		for (Terrain terrain : terrains) {
			VertexArray model = terrain.getVertexArray();
			glBindTexture(GL_TEXTURE_2D, terrain.getTexturePack().getBackgroundTexture().getID());
			model.bind();
			prepareInstance(terrain);
			glDrawElements(GL_TRIANGLES, model.getIndexCount(), GL_UNSIGNED_INT, 0);
			model.unbind();
		}
	}

//*************************************Private Methods********************************//

	/**
	 * Prepares entity to be rendered
	 * 
	 * @param entity		Entity to be prepared
	 */
	private void prepareInstance(GameObject entity) {
		Matrix4 mvpMatrix = Matrix4.mul(projectionViewMatrix, entity.getTransformation().getComponent(), null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}
	
	private void prepareInstance(Terrain terrain) {
		Matrix4 modelMatrix = MathUtils.createTransformationMatrix((Vector3) terrain.getPosition(), new Vector3(0, 0, 0),
				1);
		Matrix4 mvpMatrix = Matrix4.mul(projectionViewMatrix, modelMatrix, null);
		shader.setUniform("mvpMatrix", mvpMatrix);
	}

}
