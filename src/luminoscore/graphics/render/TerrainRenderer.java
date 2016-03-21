package luminoscore.graphics.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.shaders.TerrainShader;
import luminoscore.graphics.terrains.TexturePack;
import luminoscore.graphics.terrains.plane.Terrain;
import luminoscore.util.math.matrix.Matrix4f;
import luminoscore.util.math.matrix.MatrixCreator;
import luminoscore.util.math.vector.Vector3f;

public class TerrainRenderer {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 */
	
	//Constructor Field
	private TerrainShader shader;
	
	/*
	 * @param shader Takes in the Terrain Shader
	 * @param projectionMatrix Takes in the projection matrix
	 * 
	 * Constructor
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	/*
	 * @param terrains Takes in all terrains
	 * 
	 * Renders terrains to the screen
	 */
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	/*
	 * @param terrain Prepares textured model of terrain to be rendered
	 */
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}
	
	/*
	 * @param terrain Gets textures of terrain
	 * 
	 * Binds terrain textures
	 */
	private void bindTextures(Terrain terrain) {
		TexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getnTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBlendMap().getTextureID());
	}
	
	/*
	 * @param terrain Terrain to have model matrix created for it
	 * 
	 * Loads model matrix
	 */
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MatrixCreator.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	/*
	 * Unbinds the terrain's textured model
	 */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

}
