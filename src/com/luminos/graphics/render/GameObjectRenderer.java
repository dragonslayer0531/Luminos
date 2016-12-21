package com.luminos.graphics.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.shaders.GameObjectShader;
import com.luminos.graphics.textures.ModelTexture;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.tools.Maths;

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
	private float density = 0.0035f;
	
	/**
	 * Constructor of GameObjectRenderer
	 * 
	 * @param shader			{@link GameObjectShader} that is used for rendering entities
	 * @param projectionMatrix	Projection Matrix that is used to draw the screen
	 */
	public GameObjectRenderer(GameObjectShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders entities to screen
	 * 
	 * @param entities			Defines the map of entities to render
	 * @param shadowMapSpace	Matrix defining the shadow map transformation
	 */
	public void render(Map<TexturedModel, List<GameObject>> entities, Matrix4f shadowMapSpace) {
		shader.loadToShadowMapSpace(shadowMapSpace);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<GameObject> batch = entities.get(model);
			for (GameObject entity : batch) {
				prepareInstance(entity);
				if(model.getTexture().isDoubleSided()) {
					GL11.glFrontFace(GL11.GL_CW);
					GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
							GL11.GL_UNSIGNED_INT, 0);
				}
				GL11.glFrontFace(GL11.GL_CCW);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
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
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.hasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.usesFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadDensity(density);
		shader.loadGradient(gradient);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	/**
	 * Unbinds the prepared textured model
	 */
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	/**
	 * @param entity	GameObject to be rendered
	 * 
	 * Prepares instance of entity for rendering
	 */
	private void prepareInstance(GameObject entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix((Vector3f) entity.getPosition(),
				entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(0, 0);
	}

}