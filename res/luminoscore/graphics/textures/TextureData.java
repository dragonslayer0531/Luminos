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
	 * @param buffer	ByteBuffer to use
	 * @param width		Width of image
	 * @param height	Height of image
	 * 
	 * Constructor
	 */
	public TextureData(ByteBuffer buffer, int width, int height){
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return int	Width
	 * 
	 * Gets width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * @return int	Height
	 * 
	 * Gets height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * @return int	Buffer
	 * 
	 * Gets buffer
	 */
	public ByteBuffer getBuffer(){
		return buffer;
	}

}
