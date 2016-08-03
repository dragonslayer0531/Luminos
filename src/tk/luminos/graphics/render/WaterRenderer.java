package tk.luminos.graphics.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tk.luminos.graphics.gameobjects.Camera;
import tk.luminos.graphics.gameobjects.Light;
import tk.luminos.graphics.loaders.Loader;
import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.WaterShader;
import tk.luminos.graphics.water.WaterFrameBuffers;
import tk.luminos.graphics.water.WaterTile;
import tk.luminos.tools.Maths;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Renders water
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class WaterRenderer {

	private static final float WAVE_SPEED = 0.35f;
	
	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	
	private float moveFactor = 0;
	private int dudvTexture, normalTexture;
	
	private float tiling = 5;
	private float waveStrength = 0.04f;
	private float shineDamper = 10.0f;
	private float reflectivity = 0.05f;

	/**
	 * Constructor
	 * 
	 * @param loader			Loader used to render
	 * @param shader			Shader Program used to render
	 * @param projectionMatrix	Projection matrix passed to shader
	 * @param fbos				WaterFrameBuffers
	 * @param dudv				DUDV map location
	 * @param normal			Normal map location
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos, String dudv, String normal) {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(dudv);
		normalTexture = loader.loadTexture(normal);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadSkyColor(MasterRenderer.SKY_COLOR);
		shader.loadTiling(tiling);
		shader.loadWaveStrength(waveStrength);
		shader.loadShineDamper(shineDamper);
		shader.loadReflectivity(reflectivity);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * Renders scaled water
	 * 
	 * @param water		Water Tiles to be rendered
	 * @param camera	Camera to use in rendering
	 * @param sun		Primary light source
	 */
	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun); 
		for (WaterTile tile : water) {
			if(Maths.getDistance(new Vector3f(tile.getX(), 0, tile.getZ()), camera.getPosition()) > 500) continue;
			Matrix4f modelMatrix = Maths.createWaterTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getScale());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	/**
	 * Renders equilateral water
	 * 
	 * @param water		Water tiles to render to scene
	 * @param camera	{@link Camera} to render through
	 * @param sun		Focal {@link Light} of the scene
	 */
	public void renderTile(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);
		for(WaterTile tile : water) {
			if(Maths.getDistance(new Vector3f(tile.getX(), 0, tile.getZ()), camera.getPosition()) > 500) continue;
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getFloatScale());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
	}

	/**
	 * Gets the tiling value of the water
	 * 
	 * @return	tiling value of water
	 */
	public float getTiling() {
		return tiling;
	}
	
	/**
	 * Sets the tiling value of the water
	 * 
	 * @param tiling	tiles per quad
	 */
	public void setTiling(float tiling) {
		this.tiling = tiling;
	}
	
	/**
	 * Gets the strength of the waves
	 * 
	 * @return	strength of the waves
	 */
	public float getWaveStrength() {
		return waveStrength;
	}

	/**
	 * Sets the strength of the waves
	 * 
	 * @param waveStrength		strength of waves
	 */
	public void setWaveStrength(float waveStrength) {
		this.waveStrength = waveStrength;
	}

	/**
	 * Gets the shine damper amount
	 * 
	 * @return	shine damper amount
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * Sets the shine damper amount
	 * 
	 * @param shineDamper	shine damper amount
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * Gets the reflectivity percentage
	 * 
	 * @return	reflectivity percentage
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets the reflectivity percentage
	 * 
	 * @param reflectivity	percentage of total reflection
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
//**************************************Private Methods********************************************//	

	/**
	 * Prepare to render
	 * 
	 * @param camera	Camera to prepare with
	 * @param sun		Focal light
	 */
	private void prepareRender(Camera camera, Light sun){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * 0.001;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadRenderBox(MasterRenderer.NEAR_PLANE, MasterRenderer.FAR_PLANE);
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
	 * Binds VAO
	 * 
	 * @param loader	Defines loader to use
	 */
	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}