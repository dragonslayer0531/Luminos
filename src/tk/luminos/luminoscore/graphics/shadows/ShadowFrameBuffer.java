package tk.luminos.luminoscore.graphics.shadows;

import tk.luminos.luminoscore.graphics.FrameBufferObject;

/**
 * 
 * Creates shadow frame buffer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ShadowFrameBuffer extends FrameBufferObject {

    /**
     * Constructor
     * 
     * @param width		Defines width of shadow frame buffer
     * @param height	Defines height of shadow frame buffer
     */
    public ShadowFrameBuffer(int width, int height) {
        super(width, height, FrameBufferObject.DEPTH_TEXTURE);
    }

}
