package tk.luminos.graphics.render;

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

import java.util.List;
import java.util.Map;

import tk.luminos.gameobjects.GameObject;
import tk.luminos.graphics.Material;
import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.shaders.GameObjectShader;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector2;

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
	 * @param projectionMatrix	Projection Matrix that is used to draw the screen
	 * @throws Exception 		Thrown if shader cannot be loaded
	 */
	public GameObjectRenderer(Matrix4 projectionMatrix) throws Exception {
		this.shader = new GameObjectShader();
		shader.start();
		shader.setUniform(shader.getLocation("projectionMatrix"), projectionMatrix);
		shader.setUniform(shader.getLocation("density"), density);
		shader.setUniform(shader.getLocation("gradient"), gradient);
		shader.stop();
	}

	/**
	 * Renders entities to screen
	 * 
	 * @param entities			Defines the map of entities to render
	 */
	public void render(Map<TexturedModel, List<GameObject>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<GameObject> batch = entities.get(model);
			for (GameObject entity : batch) {
				prepareInstance(entity);
				if(model.getMaterial().isRenderDoubleSided()) {
					glFrontFace(GL_CW);
					glDrawElements(GL_TRIANGLES, model.getVertexArray().getIndexCount(), GL_UNSIGNED_INT, 0);
				}
				glFrontFace(GL_CCW);
				glDrawElements(GL_TRIANGLES, model.getVertexArray().getIndexCount(),
						GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel(model);
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
	public void dispose() {
		shader.dispose();
	}
	
	/**
	 * @return the shader
	 */
	public GameObjectShader getShader() {
		return shader;
	}
	
//***********************************Private Methods*********************************//	

	/**
	 * Prepares a textured model for rendering as entity
	 * 
	 * @param model		Defines textured model to be prepared
	 */
	private void prepareTexturedModel(TexturedModel model) {
		VertexArray vao = model.getVertexArray();
		vao.bind();
		Material texture = model.getMaterial();
		shader.setUniform(shader.getLocation("numberOfRows"), texture.getRows());
		if(texture.hasTransparency()){
			SceneRenderer.disableCulling();
		}
		shader.setUniform(shader.getLocation("useFakeLighting"), texture.useFakeLighting());
		shader.setUniform(shader.getLocation("shineDamper"), texture.getShineDamper());
		shader.setUniform(shader.getLocation("reflectivity"), texture.getReflectivity());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getMaterial().getTexture().getId());
	}

	/**
	 * Unbinds the prepared textured model
	 */
	private void unbindTexturedModel(TexturedModel model) {
		SceneRenderer.enableCulling();
		model.getVertexArray().unbind();
	}

	/**
	 * @param entity	GameObject to be rendered
	 * 
	 * Prepares instance of entity for rendering
	 */
	private void prepareInstance(GameObject entity) {
		shader.setUniform(shader.getLocation("transformationMatrix"), entity.getTransformation().getComponent());
		shader.setUniform(shader.getLocation("offset"), new Vector2(0, 0));
	}

}