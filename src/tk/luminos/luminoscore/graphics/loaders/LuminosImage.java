package tk.luminos.luminoscore.graphics.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import tk.luminos.luminoscore.Debug;
import tk.luminos.luminoscore.graphics.textures.TextureData;

/**
 * 
 * Decodes PNG images and contains the formats of different images
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosImage {
	
	public enum Format {
		RGB (GL11.GL_RGB, 3),
		RGBA (GL11.GL_RGBA, 4);
		
		private int GL_FORMAT, size;
		
		/**
		 * Constructor
		 * 
		 * @param GL_FORMAT		OpenGL's Format of the image
		 * @param size			Bytes needed to hold the size of a pixel
		 */
		Format(int GL_FORMAT, int size) {
			this.GL_FORMAT = GL_FORMAT;
			this.size = size;
		}
		
		/**
		 * Gets the LWJGL id of the OpenGL image
		 * 
		 * @return	Integer describing the OpenGL format of the image
		 */
		int getGLImageFormat() {return GL_FORMAT;}
	}
	
	private int width;
	private int height;
	private ByteBuffer buffer;
	private final Format format;
	
	/**
	 * Private constructor holding image data
	 * 
	 * @param width		Integer describing width of texture
	 * @param height	Integer describing height of texture
	 * @param buffer	ByteBuffer containing the bytes of the texture
	 * @param format	Format of the image
	 */
	private LuminosImage(int width, int height, ByteBuffer buffer, Format format) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
		this.format = format;
	}

	/**
	 * Loads file to constructor
	 * 
	 * @param file		File to load to texture
	 * @param format	Format of the file
	 * @return			LuminosImage containing file data
	 */
	public static LuminosImage loadImage(String file, Format format) {
		TextureData data = getData(file, format);
		return new LuminosImage(data.getWidth(), data.getHeight(), data.getBuffer(), format);
	}
	
	/**
	 * Loads file to texture data
	 * 
	 * @param file		File to load
	 * @param format	Format of the file
	 * @return			TextureData containing the width, height, and buffer of data
	 */
	private static TextureData getData(String file, Format format) {
		
		int width = 0, height = 0;
		
		ByteBuffer buffer = null;
		try {
			switch(format) {
			case RGBA:
				BufferedImage biRGBA = ImageIO.read(new File(file));
				width = biRGBA.getWidth();
				height = biRGBA.getHeight();
				int[] pixels = new int[width * height];
				biRGBA.getRGB(0, 0, width, height, pixels, 0, width);
				buffer = BufferUtils.createByteBuffer(width * height * format.size);
				for(int y = 0; y < height; y++){
					for(int x = 0; x < width; x++){
						int pixel = pixels[y * width + x];
						buffer.put((byte) ((pixel >> 16) & 0xFF));
						buffer.put((byte) ((pixel >> 8) & 0xFF));
						buffer.put((byte) (pixel & 0xFF));
						buffer.put((byte) ((pixel >> 24) & 0xFF));
					}
				}
				buffer.flip();
				return new TextureData(buffer, width, height);
			case RGB:
				BufferedImage biRGB = ImageIO.read(new File(file));
				width = biRGB.getWidth();
				height = biRGB.getHeight();
				int[] pixelsRGB = new int[width * height];
				biRGB.getRGB(0, 0, width, height, pixelsRGB, 0, width);
				buffer = BufferUtils.createByteBuffer(width * height * format.size);
				for(int y = 0; y < height; y++){
					for(int x = 0; x < width; x++){
						int pixel = pixelsRGB[y * width + x];
						buffer.put((byte) ((pixel >> 16) & 0xFF));
						buffer.put((byte) ((pixel >> 8) & 0xFF));
						buffer.put((byte) (pixel & 0xFF));
					}
				}
				buffer.flip();
				return new TextureData(buffer, width, height);
			}
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
		return null;	
	}

	/**
	 * Gets width of file
	 * 
	 * @return	Width of file
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets height of file
	 * 
	 * @return	Height of file
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets ByteBuffer of file
	 * 
	 * @return	Buffer containing image data
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}

	/**
	 * Gets the LuminosImage format of the image
	 * 
	 * @return	Format containing image data
	 */
	public Format getFormat() {
		return format;
	}
	
}