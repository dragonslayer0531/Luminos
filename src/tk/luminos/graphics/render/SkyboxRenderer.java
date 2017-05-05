package tk.luminos.graphics.render;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;

import tk.luminos.graphics.models.RawModel;
import tk.luminos.graphics.shaders.SkyboxShader;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;
import tk.luminos.utilities.DateUtils;

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
	private float lowerLimit = -530.0f;
	private float upperLimit = -500.0f;
	
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
	 * @throws Exception		Exception for if file isn't found or cannot be handled
	 */
	public SkyboxRenderer(SkyboxShader shader, Loader loader, Matrix4 projectionMatrix) throws Exception {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		this.shader = shader;
		shader.start();
		shader.connectTextureUnits();
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("lowerLimit", lowerLimit);
		shader.setUniform("upperLimit", upperLimit);
		shader.stop();
	}
	
	/**
	 * Render skybox
	 * 
	 * @param viewMatrix	View matrix of relevant camera
	 * @param skyColor 		SkyColor
	 */
	public void render(Matrix4 viewMatrix, Vector3 skyColor){
		shader.start();
		shader.setUniform("fogColor", skyColor);
		shader.setUniform("viewMatrix", shader.createViewMatrix(viewMatrix));
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
		bindTextures();
		glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glDisable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
		glBindVertexArray(0);
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
	
	/**
	 * Disposes of resources held by SkyboxRenderer
	 */
	public void dispose() {
		shader.dispose();
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

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
		shader.setUniform("blendFactor", blendFactor);
	}
	
}
