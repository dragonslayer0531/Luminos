package luminoscore.graphics.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.entities.Light;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.shaders.WaterShader;
import luminoscore.graphics.water.WaterFrameBuffers;
import luminoscore.graphics.water.WaterTile;
import luminoscore.tools.Maths;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Renders water
 *
 */

public class WaterRenderer {

	private final String DUDV_MAP;
	private final String NORMAL_MAP;
	private static final float WAVE_SPEED = 0.05f;
	
	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	
	private float moveFactor = 0;
	private int dudvTexture, normalTexture;

	/**
	 * @param loader			Loader used to render
	 * @param shader			Shader Program used to render
	 * @param projectionMatrix	Projection matrix passed to shader
	 * @param fbos				WaterFrameBuffers
	 * 
	 * Constructor
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos, String dudv, String normal) {
		this.shader = shader;
		this.fbos = fbos;
		this.DUDV_MAP = dudv;
		this.NORMAL_MAP = normal;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalTexture = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * @param water		Water Tiles to be rendered
	 * @param camera	Camera to use in rendering
	 * @param sun		Primary light source
	 * 
	 * Renders scaled water
	 */
	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun); 
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createWaterTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getScale());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	/**
	 * @param water
	 * @param camera
	 * @param sun
	 * 
	 * Renders equilateral water
	 */
	public void renderTile(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);
		for(WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getFloatScale());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
	}

//**************************************Private Methods********************************************//
	
	/**
	 * @param camera	Camera to prepare with
	 * @param sun		Focal light
	 * 
	 * Prepare to render
	 */
	private void prepareRender(Camera camera, Light sun){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * 0.001;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Unbind VAO
	 */
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * @param loader	Defines loader to use
	 * 
	 * Binds VAO
	 */
	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}