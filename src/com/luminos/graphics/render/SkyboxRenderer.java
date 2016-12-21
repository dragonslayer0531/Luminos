package com.luminos.graphics.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.luminos.graphics.gameobjects.Camera;
import com.luminos.graphics.loaders.Loader;
import com.luminos.graphics.models.RawModel;
import com.luminos.graphics.shaders.SkyboxShader;
import com.luminos.maths.matrix.Matrix4f;
import com.luminos.maths.vector.Vector3f;
import com.luminos.tools.DateUtils;

/**
 * 
 * Renderer for skybox
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class SkyboxRenderer {
	
	private static final float SIZE = 700f;
	private float lowerLimit = -130.0f;
	private float upperLimit = -100.0f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"res/skybox/right.png", "res/skybox/left.png", "res/skybox/top.png", "res/skybox/bottom.png", "res/skybox/back.png", "res/skybox/front.png"};
	private static String[] NIGHT_TEXTURE_FILES = {"res/skybox/nightRight.png", "res/skybox/nightLeft.png", "res/skybox/nightTop.png", "res/skybox/nightBottom.png", "res/skybox/nightBack.png", "res/skybox/nightFront.png"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float totalTime;
	
	
	/**
	 * Constructor
	 * 
	 * @param shader			Defines shader to render with
	 * @param loader			Loader used for rendering
	 * @param projectionMatrix	Projection matrix of skybox
	 */
	public SkyboxRenderer(SkyboxShader shader, Loader loader, Matrix4f projectionMatrix){
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		this.shader = shader;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Render skybox
	 * 
	 * @param camera	Camera to be projected from
	 * @param skyColor 	SkyColor
	 */
	public void render(Camera camera, Vector3f skyColor){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(skyColor);
		shader.loadLowerLimit(lowerLimit);
		shader.loadUpperLimit(upperLimit);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL11.glDisable(GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Gets the lower limit
	 * 
	 * @return	lower limit
	 */
	public float getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * Sets the lower limit
	 * 
	 * @param lowerLimit 	Lower rendering limit
	 */
	public void setLowerLimit(float lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * Gets the upper limit
	 * 
	 * @return	upper limit
	 */
	public float getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Sets the upper limit
	 * 
	 * @param upperLimit	Upper rendering limit
	 */
	public void setUpperLimit(float upperLimit) {
		this.upperLimit = upperLimit;
	}
	
//**************************************Private Methods************************************//	

	/**
	 * Binds textures of skybox
	 */
	private void bindTextures() {
		int texture1;
		int texture2;
		float blendFactor;
		totalTime = DateUtils.getTimeOfDaySeconds();
		if(totalTime >= 72000000 && totalTime < 25200000){
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = Math.abs((totalTime - 72000000))/(25200000 - 72000000);
		}else if(totalTime >= 25200000 && totalTime < 28800000){
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = Math.abs((totalTime - 25200000))/(28800000 - 25200000);
		}else if(totalTime >= 28200000 && totalTime < 68400000){
			texture1 = texture;
			texture2 = texture;
			blendFactor = Math.abs((totalTime - 28800000))/(68400000 - 28800000);
		}else{
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = Math.abs((totalTime - 68400000))/(72000000 - 68400000);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
	
}