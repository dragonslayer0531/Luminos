package com.luminos.graphics.shadows;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.luminos.ConfigData;

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
	private int fboID;
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
    
    public void bindFrameBuffer() {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
    	GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }
    
    public void unbindFrameBuffer() {
    	GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    	GL11.glViewport(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT);
    }
    
    public int getShadowMap() {
    	return shadowMap;
    }
    
    public void cleanUp() {
    	GL30.glDeleteFramebuffers(fboID);
    	GL11.glDeleteTextures(shadowMap);
    }
    
    private void initialiseFrameBuffer() {
    	fboID = createFrameBuffer();
    	shadowMap = createDepthBufferAttachment();
    }
    
    private int createFrameBuffer() {
    	int frameBuffer = GL30.glGenFramebuffers();
    	GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
    	GL11.glDrawBuffer(GL11.GL_NONE);
    	return frameBuffer;
    }
    
    private int createDepthBufferAttachment() {
    	int texture = GL11.glGenTextures();
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, WIDTH, HEIGHT, 0,
                GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;

    }

}
