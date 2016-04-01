package luminoscore.graphics.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;

import luminoscore.graphics.entities.Camera;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.shaders.SkyboxShader;
import luminoscore.tools.DateUtils;


public class SkyboxRenderer {
	
	private static final String VERT = "res/shaders/skybox.vert";
	private static final String FRAG = "res/shaders/skybox.frag";
	
	private static final float SIZE = 500f;
	
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
	
	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private DateUtils du;
	private float totalTime;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader(VERT, FRAG);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare(DateUtils du) {
		this.du = du;
	}
	
	public void render(Camera camera, float r, float g, float b){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(r, g, b);
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
	
	private void bindTextures() {
		int texture1;
		int texture2;
		float blendFactor;
		totalTime = du.getTotalTime();
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
