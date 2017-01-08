package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glFrontFace;
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
import com.luminos.graphics.shaders.GameObjectShader;
import com.luminos.graphics.textures.Material;
import com.luminos.tools.Maths;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector2f;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * Allows for rendering of entities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GameObjectRenderer {

	private GameObjectShader shader;
	private float gradient = 5.0f;
	private float density = 0.001f;
	
	/**
	 * Constructor of GameObjectRenderer
	 * 
	 * @param shader			{@link GameObjectShader} that is used for rendering entities
	 * @param projectionMatrix	Projection Matrix that is used to draw the screen
	 */
	public GameObjectRenderer(GameObjectShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.setUniform(shader.getLocation("projectionMatrix"), projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders entities to screen
	 * 
	 * @param entities			Defines the map of entities to render
	 * @param shadowMapSpace	Matrix defining the shadow map transformation
	 */
	public void render(Map<TexturedModel, List<GameObject>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<GameObject> batch = entities.get(model);
			for (GameObject entity : batch) {
				prepareInstance(entity);
				if(model.getMaterial().isRenderDoubleSided()) {
					glFrontFace(GL_CW);
					glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
				}
				glFrontFace(GL_CCW);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	/**
	 * Gets the gradient of the fog
	 * 
	 * @return	gradient of the fog
	 */
	public float getGradient() {
		return gradient;
	}

	/**
	 * Sets the gradient of the fog
	 * 
	 * @param gradient		fog gradient value
	 */
	public void setGradient(float gradient) {
		this.gradient = gradient;
	}

	/**
	 * Gets the density of the fog
	 * 
	 * @return	density of the fog
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * Sets the density of the fog
	 * 
	 * @param density	fog density value
	 */
	public void setDensity(float density) {
		this.density = density;
	}
	
	/**
	 * Cleans up shader program
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
	
//***********************************Private Methods*********************************//	

	/**
	 * Prepares a textured model for rendering as entity
	 * 
	 * @param model		Defines textured model to be prepared
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		Material texture = model.getMaterial();
		shader.setUniform(shader.getLocation("numberOfRows"), texture.getRows());
		if(texture.hasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.setUniform(shader.getLocation("useFakeLighting"), texture.useFakeLighting());
		shader.setUniform(shader.getLocation("shineDamper"), texture.getShineDamper());
		shader.setUniform(shader.getLocation("reflectivity"), texture.getReflectivity());
		shader.setUniform(shader.getLocation("density"), density);
		shader.setUniform(shader.getLocation("gradient"), gradient);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getMaterial().getDiffuseID());
	}

	/**
	 * Unbinds the prepared textured model
	 */
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	/**
	 * @param entity	GameObject to be rendered
	 * 
	 * Prepares instance of entity for rendering
	 */
	private void prepareInstance(GameObject entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix((Vector3f) entity.getPosition(),
				entity.getRotation(), entity.getScale());
		shader.setUniform(shader.getLocation("transformationMatrix"), transformationMatrix);
		shader.setUniform(shader.getLocation("offset"), new Vector2f(0, 0));
	}

}