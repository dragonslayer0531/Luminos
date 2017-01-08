package com.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.gameobjects.PointLight;
import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.shaders.WaterShader;
import com.luminos.graphics.water.WaterFrameBuffers;
import com.luminos.graphics.water.WaterTile;
import com.luminos.loaders.Loader;
import com.luminos.tools.Maths;
import com.luminos.tools.maths.matrix.Matrix4f;
import com.luminos.tools.maths.vector.Vector3f;

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
	
	private int   tiling = 5;
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
	 * @throws IOException		Exception for if file isn't found or cannot be handled
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos, String dudv, String normal) throws IOException {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(dudv);
		normalTexture = loader.loadTexture(normal);
		shader.start();
		shader.connectTextureUnits();
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("skyColor", MasterRenderer.SKY_COLOR);
		shader.setUniform("tiling", tiling);
		shader.setUniform("waveStrength", waveStrength);
		shader.setUniform("shineDamper", shineDamper);
		shader.setUniform("reflectivity", reflectivity);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * Renders scaled water
	 * 
	 * @param water		Water Tiles to be rendered
	 * @param camera	Camera to use in rendering
	 * @param lights		Primary light source
	 */
	public void render(List<WaterTile> water, Camera camera, List<PointLight> lights) {
		if (lights == null) {
			lights = new ArrayList<PointLight>();
		}
		for (PointLight light : lights) {
			prepareRender(camera, light); 
			for (WaterTile tile : water) {
				if (Maths.getDistance(new Vector3f(tile.getX(), 0, tile.getZ()), camera.getPosition()) > 500) continue;
				Matrix4f modelMatrix = Maths.createWaterTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getScale());
				shader.setUniform("modelMatrix", modelMatrix);
				glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
			}
		}
		unbind();
	}
	
	/**
	 * Renders equilateral water
	 * 
	 * @param water		Water tiles to render to scene
	 * @param camera	{@link Camera} to render through
	 * @param sun		Focal {@link PointLight} of the scene
	 */
	public void renderTile(List<WaterTile> water, Camera camera, PointLight sun) {
		prepareRender(camera, sun);
		for (WaterTile tile : water) {
			if (Maths.getDistance(new Vector3f(tile.getX(), 0, tile.getZ()), camera.getPosition()) > 500) continue;
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, tile.getFloatScale());
			shader.setUniform("modelMatrix", modelMatrix);
			glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
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
	public void setTiling(int tiling) {
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
	private void prepareRender(Camera camera, PointLight sun){
		shader.start();
		shader.setUniform("viewMatrix", Maths.createViewMatrix(camera));
		shader.setUniform("cameraPosition", camera.getPosition());
		moveFactor += WAVE_SPEED * 0.001;
		moveFactor %= 1;
		shader.setUniform("moveFactor", moveFactor);
		shader.setUniform("near", MasterRenderer.NEAR_PLANE);
		shader.setUniform("far", MasterRenderer.FAR_PLANE);
		shader.loadPointLight(sun);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, fbos.getReflectionTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, fbos.getRefractionTexture());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, dudvTexture);
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, normalTexture);
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Unbind VAO
	 */
	
	private void unbind(){
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
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
