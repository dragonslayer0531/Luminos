package luminoscore.graphics.textures;

import java.nio.ByteBuffer;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Texture Data of Buffers
 *
 */

public class TextureData {
	
	private int width;
	private int height;
	private ByteBuffer buffer;
	
	/**
	 * Constructor
	 * 
	 * @param buffer	ByteBuffer to use
	 * @param width		Width of image
	 * @param height	Height of image
	 */
	public TextureData(ByteBuffer buffer, int width, int height){
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Gets width
	 * 
	 * @return	Width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Gets height
	 * 
	 * @return	Height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Gets buffer
	 * 
	 * @return	Buffer
	 */
	public ByteBuffer getBuffer(){
		return buffer;
	}

}
