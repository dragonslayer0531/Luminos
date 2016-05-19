package tk.luminos.luminoscore.graphics.water;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import tk.luminos.luminoscore.GlobalLock;

/**
 * 
 * Water Frame Buffer Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class WaterFrameBuffers {

	protected static final int REFLECTION_WIDTH = GlobalLock.WATER_FBO_REFLEC_WIDTH;
	private static final int REFLECTION_HEIGHT = GlobalLock.WATER_FBO_REFLEC_HEIGHT;
	
	protected static final int REFRACTION_WIDTH = GlobalLock.WATER_FBO_REFRAC_WIDTH;
	private static final int REFRACTION_HEIGHT = GlobalLock.WATER_FBO_REFRAC_HEIGHT;

	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;
	
	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;
	
	/**
	 * Constructor
	 */
	public WaterFrameBuffers() {
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	/**
	 * Cleans up FBO and attached buffers
	 */
	public void cleanUp() {
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL11.glDeleteTextures(refractionTexture);
		GL11.glDeleteTextures(refractionDepthTexture);
	}

	/**
	 * Binds reflection buffer
	 */
	public void bindReflectionFrameBuffer() {
		bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
	}
	
	/**
	 * Binds refraction buffer
	 */
	public void bindRefractionFrameBuffer() {
		bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
	}
	
	/**
	 * Unbinds frame buffer
	 */
	public void unbindCurrentFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, GlobalLock.WIDTH, GlobalLock.HEIGHT);
	}

	/**
	 * Gets reflection texture
	 * 
	 * @return	Reflection Texture ID
	 */
	public int getReflectionTexture() {
		return reflectionTexture;
	}
	
	/**
	 * Gets refraction texture
	 * 
	 * @return	Refraction Texture ID
	 */
	public int getRefractionTexture() {
		return refractionTexture;
	}
	
	/**
	 * Gets refraction depth texture
	 * 
	 * @return 	Refraction Depth Texture ID
	 */
	public int getRefractionDepthTexture() {
		return refractionDepthTexture;
	}

//***********************************************Private Methods**********************************************//
	
	/**
	 * Initializes reflection frame buffer
	 */
	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	/**
	 * Initializes refraction frame buffer
	 */
	private void initialiseRefractionFrameBuffer() {
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	/**
	 * Binds frame buffer
	 * 
	 * @param frameBuffer	Frame Buffer ID
	 * @param width			Frame Buffer Width
	 * @param height		Frame Buffer Height
	 */
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Creates frame buffer
	 * 
	 * @return	VAO ID of Frame Buffer
	 */
	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return frameBuffer;
	}

	/**
	 * Creates texture attachment
	 * 
	 * @param width		Width of attachment
	 * @param height	Height of attachment
	 * @return 	GPU ID of texture attachment
	 */
	private int createTextureAttachment(int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				texture, 0);
		return texture;
	}
	
	/**
	 * Creates depth texture attachment
	 * 
	 * @param width		Width of depth attachment
	 * @param height	Height of depth attachment
	 * @return 			GPU ID of texture attachment
	 */
	private int createDepthTextureAttachment(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
				0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				texture, 0);
		return texture;
	}

	/**
	 * Creates depth buffer attachment
	 * 
	 * @param width		Width of depth buffer
	 * @param height	Height of depth buffer
	 * @return			GPU ID of buffer
	 */
	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
				height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

}
