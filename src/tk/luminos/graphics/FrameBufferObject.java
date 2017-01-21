package tk.luminos.graphics;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL30.glRenderbufferStorageMultisample;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import tk.luminos.ConfigData;
 
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
    
    /**
     * Constructor 
     * 
     * @param width				Width of the FBO
     * @param height			Height of the FBO
     */
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
        glDeleteFramebuffers(frameBuffer);
        glDeleteTextures(colorTexture);
        glDeleteTextures(depthTexture);
        glDeleteRenderbuffers(depthBuffer);
        glDeleteRenderbuffers(colorBuffer);
    }
 
    /**
     * Binds the current FBO to be drawn to
     */
    public void bindFrameBuffer() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }
 
    /**
     * Unbinds the current FBO and sets the default frame buffer as the draw buffer
     */
    public void unbindFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT);
    }
 
    /**
     * Binds the FBO to be read from
     */
    public void bindToRead() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBuffer);
        glReadBuffer(GL_COLOR_ATTACHMENT0);
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
     * Resolves the contents of the current frame buffer to another
     * 
     * @param fbo			FrameBufferObject to be resolved to
     */
    public void resolveToFBO(FrameBufferObject fbo) {
    	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo.frameBuffer);
    	glBindFramebuffer(GL_READ_FRAMEBUFFER, this.frameBuffer);
    	glBlitFramebuffer(0, 0, width, height, 0, 0, fbo.width, fbo.height, GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, GL_NEAREST);
    	this.unbindFrameBuffer();
    }
    
    /**
     * Resolves FrameBufferObject to screen
     */
    public void resolveToScreen() {
    	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    	glBindFramebuffer(GL_READ_FRAMEBUFFER, this.frameBuffer);
    	glDrawBuffer(GL_BACK);
    	glBlitFramebuffer(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT, 0, 0, ConfigData.WIDTH, ConfigData.HEIGHT, GL_COLOR_BUFFER_BIT, GL_NEAREST);
    	this.unbindFrameBuffer();
    }
 
    private void initialiseFrameBuffer(int type) {
        createFrameBuffer();
        if(multisample) {
        	colorBuffer = createMultisampleColorAttachment(GL_COLOR_ATTACHMENT0);
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
    	drawBuffers.put(GL_COLOR_ATTACHMENT0);
    	if (this.multisample) {
    		drawBuffers.put(GL_COLOR_ATTACHMENT1);
    	}
    	drawBuffers.flip();
    	glDrawBuffers(drawBuffers);
    }
 
    private void createFrameBuffer() {
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        determineDrawBuffers();
    }

    private void createTextureAttachment() {
        colorTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTexture,
                0);
    }

    private int createMultisampleColorAttachment(int attachment) {
    	int colorBuffer = glGenRenderbuffers();
    	glBindRenderbuffer(GL_RENDERBUFFER, colorBuffer);
    	glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_RGBA8, width, height);
    	glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, colorBuffer);
    	return colorBuffer;
    }

    private void createDepthTextureAttachment() {
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, width, height, 0, GL_DEPTH_COMPONENT,
                GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);
    }

    private void createDepthBufferAttachment() {
        depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        if(!multisample) {
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
        } else {
            glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_DEPTH_COMPONENT24, width, height);
        }
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER,
                depthBuffer);
    }
    
}
