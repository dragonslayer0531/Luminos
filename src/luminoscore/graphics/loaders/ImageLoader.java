package luminoscore.graphics.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class ImageLoader {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */

	//List containing textures
	private List<Integer> textures = new ArrayList<Integer>();
	
	/*
	 * Constructor
	 */
	public ImageLoader() {

	}
	
	//Cleans up GPU of textures
	public void destroy() {
		for(Integer texture : textures) {
			GL11.glDeleteTextures(texture);
		}
		
		textures.clear();
	}
	
	/*
	 * @param filename passes in filename to the file
	 * @param mipmap determines whether to use mipmapping
	 * 
	 * @return int
	 * 
	 * Loads texture to the GPU and returns a GLuint
	 */
	public int loadTexture(String filename, boolean mipmap) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Could not read file");
		}
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0,  0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		int textureID = GL11.glGenTextures();
		
		if(mipmap) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		
		textures.add(textureID);
		return textureID;
	}

}
