package tk.luminos.graphics.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.TerrainShader;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.graphics.textures.TerrainTexturePack;
import tk.luminos.tools.Maths;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Renders terrains
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class TerrainRenderer {

	private TerrainShader shader;
	private float density = 0.001f;
	private float gradient = 5.0f;
	private int tileFactor = 60;

	/**
	 * Constructor
	 * 
	 * @param shader			Shader Program that is used to render terrains
	 * @param projectionMatrix	Projectioon matrix used to render terrains
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTileFactor(tileFactor);
		shader.connectTextureUnits();
		shader.stop();
	}

	/**
	 * Renders terrains
	 * 
	 * @param terrains		List of terrains to be rendered
	 * @param toShadowSpace	Loads shadow space to shader
	 */
	public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {
		shader.loadToShadowSpaceMatrix(toShadowSpace);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			shader.loadDensity(density);
			shader.loadGradient(gradient);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
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
	 * @param density	density of fog
	 */
	public void setDensity(float density) {
		this.density = density;
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
	 * @param gradient	gradient of fog
	 */
	public void setGradient(float gradient) {
		this.gradient = gradient;
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

//**********************************Private Methods*****************************************//	
	
	/**
	 * Prepares terrains for rendering
	 * 
	 * @param terrain  Terrain to be prepared
	 */
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}
	
	/**
	 * Binds textures
	 * 
	 * @param terrain	Reference to textures to be bound
	 */
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getID());
	}

	/**
	 * Unbinds textured model
	 */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Loads model matrix to shader
	 * 
	 * @param terrain	Terrain to calculate Model Matrix of
	 */
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
