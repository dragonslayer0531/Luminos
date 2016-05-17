package luminoscore.graphics.shadows;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import luminoscore.GlobalLock;

/**
 * 
 * Creates shadow frame buffer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowFrameBuffer {
	
	private final int WIDTH;
    private final int HEIGHT;
    private int fbo;
    private int shadowMap;

    /**
     * Constructor
     * 
     * @param width		Defines width of shadow frame buffer
     * @param height	Defines height of shadow frame buffer
     */
    public ShadowFrameBuffer(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        initialiseFrameBuffer();
    }

    /**
     * Deletes frame bufffer and shadow map  texture
     */
    public void cleanUp() {
        GL30.glDeleteFramebuffers(fbo);
        GL11.glDeleteTextures(shadowMap);
    }

    /**
     * Binds frame buffers
     */
    public void bindFrameBuffer() {
        bindFrameBuffer(fbo, WIDTH, HEIGHT);
    }

    /** 
     * Unbinds frame buffer
     */
    public void unbindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, GlobalLock.WIDTH, GlobalLock.HEIGHT);
    }

    /**
     * Gets shadow map ID
     * 
     * @return Shadow Map ID
     */
    public int getShadowMap() {
        return shadowMap;
    }

//*************************************Private Methods***************************************//
    
    /** 
     * Initializes frame buffers
     */
    private void initialiseFrameBuffer() {
        fbo = createFrameBuffer();
        shadowMap = createDepthBufferAttachment(WIDTH, HEIGHT);
        unbindFrameBuffer();
    }
    
    /**
     * Binds frame buffer to texture
     * 
     * @param frameBuffer	Frame Buffer ID
     * @param width			Width of frame buffer
     * @param height		Height of frame buffer
     */
    private static void bindFrameBuffer(int frameBuffer, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Create frame buffer object
     * 
     * @return 	ID of frame buffer
     */
    private static int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glDrawBuffer(GL11.GL_NONE);
        return frameBuffer;
    }

    /**
     * Creates a depth buffer attachment
     * 
     * @param width		Width of depth buffer
     * @param height	Height of depth buffer
     * @return 			Depth buffer attachment ID
     */
    private static int createDepthBufferAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, width, height, 0,
                GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;
    }

}
