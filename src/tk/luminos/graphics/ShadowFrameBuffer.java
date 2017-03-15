package tk.luminos.graphics;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.ByteBuffer;

import tk.luminos.ConfigData;

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
    	glBindTexture(GL_TEXTURE_2D, 0);
    	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboID);
    	glViewport(0, 0, WIDTH, HEIGHT);
    }
    
    public void unbindFrameBuffer() {
    	glBindFramebuffer(GL_FRAMEBUFFER, 0);
    	glViewport(0, 0, ConfigData.WIDTH, ConfigData.HEIGHT);
    }
    
    public int getShadowMap() {
    	return shadowMap;
    }
    
    public void cleanUp() {
    	glDeleteFramebuffers(fboID);
    	glDeleteTextures(shadowMap);
    }
    
    private void initialiseFrameBuffer() {
    	fboID = createFrameBuffer();
    	shadowMap = createDepthBufferAttachment();
    }
    
    private int createFrameBuffer() {
    	int frameBuffer = glGenFramebuffers();
    	glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
    	glDrawBuffer(GL_NONE);
    	return frameBuffer;
    }
    
    private int createDepthBufferAttachment() {
    	int texture = glGenTextures();
    	glBindTexture(GL_TEXTURE_2D, texture);
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, WIDTH, HEIGHT, 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;

    }

}
