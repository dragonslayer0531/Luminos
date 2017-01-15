package tk.luminos.graphics.water;

import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_WIDTH;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_WIDTH;
import static tk.luminos.ConfigData.WIDTH;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.ByteBuffer;


/**
 * 
 * Water Frame Buffer Objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class WaterFrameBuffers {

	protected static final int REFLECTION_WIDTH = WATER_FBO_REFLEC_WIDTH;
	private static final int REFLECTION_HEIGHT = WATER_FBO_REFLEC_HEIGHT;
	
	protected static final int REFRACTION_WIDTH = WATER_FBO_REFRAC_WIDTH;
	private static final int REFRACTION_HEIGHT = WATER_FBO_REFRAC_HEIGHT;

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
		glDeleteFramebuffers(reflectionFrameBuffer);
		glDeleteTextures(reflectionTexture);
		glDeleteRenderbuffers(reflectionDepthBuffer);
		glDeleteFramebuffers(refractionFrameBuffer);
		glDeleteTextures(refractionTexture);
		glDeleteTextures(refractionDepthTexture);
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
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, WIDTH, HEIGHT);
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
		glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
	}

	/**
	 * Creates frame buffer
	 * 
	 * @return	VAO ID of Frame Buffer
	 */
	private int createFrameBuffer() {
		int frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
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
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height,
				0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
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
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height,
				0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
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
		int depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width,
				height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

}
