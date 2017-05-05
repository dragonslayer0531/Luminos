package tk.luminos.loaders;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import tk.luminos.graphics.TextureData;

/**
 * 
 * Decodes PNG images and contains the formats of different images
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosImage {

	/**
	 * Enumeration of image formats
	 * 
	 * @author Nick Clark
	 * @version 1.0
	 */
	public enum Format {
		RGB (GL_RGB, 3),
		RGBA (GL_RGBA, 4);

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
	 * @param file			File to load to texture
	 * @param format		Format of the file
	 * @return				LuminosImage containing file data
	 * @throws Exception 	Exception for if file isn't found or cannot be handled
	 */
	public static LuminosImage loadImage(String file, Format format) throws Exception {
		TextureData data = getData(file, format);
		return new LuminosImage(data.getWidth(), data.getHeight(), data.getBuffer(), format);
	}
	
	/**
	 * Loads image to memory
	 * 
	 * @param image		buffered image to load
	 * @param format	format of image
	 * @return			Luminos image representing image
	 */
	public static LuminosImage loadImage(BufferedImage image, Format format) {
		TextureData data = getData(image, format);
		return new LuminosImage(data.getWidth(), data.getHeight(), data.getBuffer(), format);
	}

	/**
	 * Loads file to texture data
	 * 
	 * @param file			File to load
	 * @param format		Format of the file
	 * @return				TextureData containing the width, height, and buffer of data
	 * @throws Exception 	Exception for if file isn't found or cannot be handled
	 */
	@SuppressWarnings("unused")
	private static TextureData getData(String file, Format format) throws Exception {
		// PNG or JPG files
		if (file.endsWith(".png") || file.endsWith(".jpg")) {
			int width = 0, height = 0;

			ByteBuffer buffer = null;
			switch(format) {
			case RGBA:
				BufferedImage biRGBA = null;
				try {
					biRGBA = ImageIO.read(new File(file));
					System.out.println(file + " " + biRGBA.getColorModel().toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new Exception("Could not find file: " + file + "\n");
				}
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
				BufferedImage biRGB = null;
				try {
					biRGB = ImageIO.read(new File(file));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new Exception("Could not find file: " + file + "\n");
				}
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
			
			return null;
		}
		else if (file.endsWith(".tga")) {
			byte red = 0;
			byte green = 0;
			byte blue = 0;
			byte alpha = 0;
			InputStream fis = new FileInputStream(new File(file));
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			
			short idLength = (short) dis.read();
			short colorMapType = (short) dis.read();
			short imageType = (short) dis.read();
			short cMapStart = flipEndian(dis.readShort());
			short cMapLength = flipEndian(dis.readShort());
			short cMapDepth = (short) dis.read();
			short xOffset = flipEndian(dis.readShort());
			short yOffset = flipEndian(dis.readShort());
			
			if (imageType != 2) {
				fis.close();
				dis.close();
				bis.close();
				throw new Exception("Luminos only supports RGB(A) TGA formats");
			}
			
			int width = (int) flipEndian(dis.readShort());
			int height = (int) flipEndian(dis.readShort());
			
			int pixelDepth = (short) dis.read();
			
			boolean forceAlpha = true;
			
			if (pixelDepth == 32)
				forceAlpha = false;
			int texWidth = get2Fold(width);
			int texHeight = get2Fold(height);
			
			short descript = (short) dis.read();
			
			boolean flipped = false;
			if ((descript & 0x0020) == 0)
				flipped = !flipped;
			
			if (idLength > 0)
				bis.skip(idLength);
			
			byte[] rawData = null;
			if ((pixelDepth == 32) || (forceAlpha))
			{
				pixelDepth = 32;
				rawData = new byte[texWidth * texHeight * 4];
			}
			else if (pixelDepth == 24)
			{
				rawData = new byte[texWidth * texHeight * 3];
			}
			else 
			{
				fis.close();
				dis.close();
				bis.close();				
				throw new Exception("Only 24 and 32 bit TGA formats are supported");
			}
			
			if (pixelDepth == 24) {
				if (!flipped) {
					for (int i = 0; i < height; i++) {
						for (int j = 0; j < height; j++) {
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							
							int ofs = ((j + (i * texWidth)) * 3);
							rawData[ofs] = red;
							rawData[ofs + 1] = green;
							rawData[ofs + 2] = blue;
						}
					}
				}
				else {
					for (int i = width - 1; i >= 0; i--) {
						for (int j = height - 1; j >= 0; j--) {
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							
							int ofs = ((j + (i * texWidth)) * 3);
							rawData[ofs] = red;
							rawData[ofs + 1] = green;
							rawData[ofs + 2] = blue;
						}
					}
				}
			} 
			else if (pixelDepth == 32) {
				if (flipped) {
					for (int i = width - 1; i >= 0; i--) {
						for (int j = height - 1; j >= 0; j--) {
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							if (forceAlpha)
								alpha = (byte) 255;
							else
								alpha = dis.readByte();
							
							int ofs = ((j + (i * texWidth)) * 4);
							rawData[ofs] = red;
							rawData[ofs + 1] = green;
							rawData[ofs + 2] = blue;
							rawData[ofs + 3] = alpha;
							
							if (alpha == 0) {
								rawData[ofs] = (byte) 0;
								rawData[ofs + 1] = (byte) 0;
								rawData[ofs + 2] = (byte) 0;
							}
						}
					}
				}
				else {
					for (int i = 0; i < height; i++) {
						for (int j = 0; j < width; j++) {
							blue = dis.readByte();
							green = dis.readByte();
							red = dis.readByte();
							if (forceAlpha) {
								alpha = (byte) 255;
							} else {
								alpha = dis.readByte();
							}
							
							int ofs = ((j + (i * texWidth)) * 4);
							
							if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
								rawData[ofs] = red;
								rawData[ofs + 1] = green;
								rawData[ofs + 2] = blue;
								rawData[ofs + 3] = alpha;
							} else {
								rawData[ofs] = red;
								rawData[ofs + 1] = green;
								rawData[ofs + 2] = blue;
								rawData[ofs + 3] = alpha;
							}
							
							if (alpha == 0) {
								rawData[ofs + 2] = 0;
								rawData[ofs + 1] = 0;
								rawData[ofs] = 0;
							}
						}
					}
				}
			}
			fis.close();
			
			ByteBuffer scratch = BufferUtils.createByteBuffer(rawData.length);
			scratch.put(rawData);
			
			int perPixel = pixelDepth / 8;
			if (height < texHeight - 1) {
				int topOffset = (texHeight - 1) * (texWidth * perPixel);
				int bottomOffset = (height - 1) * (texWidth * perPixel);
				for (int x = 0; x < texWidth * perPixel; x++) {
					scratch.put(topOffset + x, scratch.get(x));
					scratch.put(bottomOffset + (texWidth * perPixel) + x, scratch.get((texWidth * perPixel) * x));
				}
			}
			if (width < texWidth - 1) {
				for (int y = 0; y < texHeight; y++) {
					for (int i = 0; i < perPixel; i++) {
						scratch.put(((y  + 1) * (texWidth * perPixel)) - perPixel + i, scratch.get(y *(texWidth * perPixel) + i));
						scratch.put((y * (texWidth * perPixel) + (width * perPixel)) + i, scratch.get(y * (texWidth * perPixel) + ((width - 1) * perPixel) + i));
					}
				}
			}
			scratch.flip();
			
			return new TextureData(scratch, width, height);
		}
		throw new Exception("Unsupported Image Format");
	}
	
	private static TextureData getData(BufferedImage image, Format format) {
		int width = 0, height = 0;

		ByteBuffer buffer = null;
		switch(format) {
		case RGBA:
			width = image.getWidth();
			height = image.getHeight();
			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
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
			width = image.getWidth();
			height = image.getHeight();
			int[] pixelsRGB = new int[width * height];
			image.getRGB(0, 0, width, height, pixelsRGB, 0, width);
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
	
	private static short flipEndian(short signedShort) {
		int input = signedShort & 0xFFFF;
		return (short) (input << 8 | (input & 0xFF00) >>> 8);
	}
	
	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold)
			ret *= 2;
		return ret;
	}

}