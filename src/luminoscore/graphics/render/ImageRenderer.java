package luminoscore.graphics.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import luminoscore.GlobalLock;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.shaders.ImageShader;

/**
 * 
 * Renders an image to a screen
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ImageRenderer {
	
	private static final float[] POSITIONS = {
			-1, 1, -1, -1, 1, 1, 1, -1
	};
	private RawModel quad;
	private ImageShader shader;
	private FrameBufferObject fbo;
	
	/**
	 * Constructor
	 * 
	 * @param loader	Loader to use to render
	 */
	public ImageRenderer(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		shader = new ImageShader();
		fbo = new FrameBufferObject(GlobalLock.WIDTH, GlobalLock.HEIGHT, FrameBufferObject.NONE);
	}
	
	/**
	 * Renders texture to screen
	 * 
	 * @param texture	Index of the texture on the GPU
	 */
	public void render(int texture) {
		shader.start();
		
		if(fbo != null) {
			fbo.bindFrameBuffer();
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	
		if(fbo != null) {
			fbo.unbindFrameBuffer();
		}
		
		shader.stop();
	}
	
	/**
	 * Finished the rendering
	 */
	public void finish() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(GlobalLock.POSITION);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Cleans up the shader
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

	/**
	 * Gets the quad to be rendered to
	 * 
	 * @return	Quad to be rendered to
	 */
	public RawModel getQuad() {
		return quad;
	}

}
