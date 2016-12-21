package com.luminos.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.luminos.ConfigData;
 
/**
 * 
 * Creates Frame Buffer Objects to render to
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */
public class FrameBufferObject {
 
    public static final int NONE = 0;
    public static final int DEPTH_TEXTURE = 1;
    public static final int DEPTH_RENDER_BUFFER = 2;
 
    private final int width;
    private final int height;
 
    private int frameBuffer;
 
    private int colorTexture;
    private int depthTexture;
 
    private int depthBuffer;
    private int colorBuffer;
    private int colorBuffer2;
    
    private boolean multisample = false;
 
    /**
     * Constructor
     * 
     * @param width				the width of the FBO.
     * @param height			the height of the FBO.
     * @param depthBufferType	int indicating the type of depth buffer attachment that this FBO should use.
     */
    public FrameBufferObject(int width, int height, int depthBufferType) {
        this.width = width;
        this.height = height;
        initialiseFrameBuffer(depthBufferType);
    }
    
    public FrameBufferObject(int width, int height) {
        this.width = width;
        this.height = height;
        this.multisample = true;
        initialiseFrameBuffer(DEPTH_RENDER_BUFFER);
    }
 
    /**
     * Deletes the frame buffer and its attachments when the game closes.
     */
    public void cleanUp() {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colorTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer2);
    }
 
    /**
     * Binds the frame buffer, setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     */
    public void bindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }
 
    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO.
     */
    public void unbindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT);
    }
 
    /**
     * Binds the current FBO to be read from (not used in tutorial 43).
     */
    public void bindToRead() {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }
 
    /**
     * @return The ID of the texture containing the color buffer of the FBO.
     */
    public int getColorTexture() {
        return colorTexture;
    }
 
    /**
     * @return The texture containing the FBOs depth buffer.
     */
    public int getDepthTexture() {
        return depthTexture;
    }
    
    /**
     * Resolves frame buffer to another frame buffer
     * 
     * @param readBuffer	Buffer to read from
     * @param fbo			FrameBufferObject to be resolved to
     */
    public void resolveToFBO(int readBuffer, FrameBufferObject fbo) {
    	GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo.frameBuffer);
    	GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
    	GL11.glReadBuffer(readBuffer);
    	GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, fbo.width, fbo.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
    	this.unbindFrameBuffer();
    }
    
    /**
     * Resolves FrameBufferObject to screen
     */
    public void resolveToScreen() {
    	GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    	GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
    	GL11.glDrawBuffer(GL11.GL_BACK);
    	GL30.glBlitFramebuffer(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT, 0, 0, ConfigData.WIDTH, ConfigData.HEIGHT, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
    	this.unbindFrameBuffer();
    }
 
    /**
     * Creates the FBO along with a color buffer texture attachment, and
     * possibly a depth buffer.
     * 
     * @param type
     *            - the type of depth buffer attachment to be attached to the
     *            FBO.
     */
    private void initialiseFrameBuffer(int type) {
        createFrameBuffer();
        if(multisample) {
        	colorBuffer = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
        	colorBuffer2 = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT1);
        } else {
        	createTextureAttachment();
        }
        if (type == DEPTH_RENDER_BUFFER) {
            createDepthBufferAttachment();
        } else if (type == DEPTH_TEXTURE) {
            createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }
    
    private void determineDrawBuffers() {
    	IntBuffer drawBuffers = BufferUtils.createIntBuffer(2);
    	drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
    	if (this.multisample) {
    		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
    	}
    	drawBuffers.flip();
    	GL20.glDrawBuffers(drawBuffers);
    }
 
    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur - color attachment 0. This is the attachment where the color
     * buffer texture is.
     * 
     */
    private void createFrameBuffer() {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        determineDrawBuffers();
    }
 
    /**
     * Creates a texture and sets it as the color buffer attachment for this
     * FBO.
     */
    private void createTextureAttachment() {
        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture,
                0);
    }
    
    /**
     * Creates a color attachment with multiple samples
     */
    private int createMultisampleColorAttachment(int attachment) {
    	int colorBuffer = GL30.glGenRenderbuffers();
    	GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
    	GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL11.GL_RGBA8, width, height);
    	GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, colorBuffer);
    		return colorBuffer;
    }
 
    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     */
    private void createDepthTextureAttachment() {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
                GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }
 
    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment() {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        if(!multisample) {
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
        } else {
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL14.GL_DEPTH_COMPONENT24, width, height);
        }
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
                depthBuffer);
    }
    
}
