package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import tk.luminos.gameobjects.Terrain;
import tk.luminos.graphics.TerrainTexturePack;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.TerrainShader;
import tk.luminos.maths.MathUtils;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

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
	public TerrainRenderer(TerrainShader shader, Matrix4 projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("tileFactor", tileFactor);
		shader.setUniform("density", density);
		shader.setUniform("gradient", gradient);
		shader.setUniform("shineDamper", 1f);
		shader.setUniform("reflectivity", 0f);
		shader.setUniform("pcfCount", 2);
		shader.setUniform("shadowDistance", 200.0f);
		shader.setUniform("transitionDistance", 20.0f);
		shader.connectTextureUnits();
		shader.stop();
	}

	/**
	 * Renders terrains
	 * 
	 * @param terrains		List of terrains to be rendered
	 * @param toShadowSpace	Loads shadow space to shader
	 * @param shadowMap		Shadow map texture id
	 */
	public void render(List<Terrain> terrains, Matrix4 toShadowSpace, int shadowMap) {
		shader.setUniform("toShadowMapSpace", toShadowSpace);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain, shadowMap);
			loadModelMatrix(terrain);
			glDrawElements(GL_TRIANGLES, terrain.getRawModel().getVertexCount(),
					GL_UNSIGNED_INT, 0);
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
	
	public void dispose() {
		shader.dispose();
	}

//**********************************Private Methods*****************************************//	
	
	/**
	 * Prepares terrains for rendering
	 * 
	 * @param terrain  Terrain to be prepared
	 */
	private void prepareTerrain(Terrain terrain, int shadowMap) {
		RawModel rawModel = terrain.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		bindTextures(terrain, shadowMap);
	}
	
	/**
	 * Binds textures
	 * 
	 * @param terrain	Reference to textures to be bound
	 */
	private void bindTextures(Terrain terrain, int shadowMap){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texturePack.getrTexture().getID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, texturePack.getgTexture().getID());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, texturePack.getbTexture().getID());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getID());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, shadowMap);
	}

	/**
	 * Unbinds textured model
	 */
	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	/**
	 * Loads model matrix to shader
	 * 
	 * @param terrain	Terrain to calculate Model Matrix of
	 */
	private void loadModelMatrix(Terrain terrain) {
		Matrix4 transformationMatrix = MathUtils.createTransformationMatrix(
				new Vector3(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.setUniform("transformationMatrix", transformationMatrix);
	}

}
