package com.luminos.loaders;

import static org.lwjgl.opengl.GL.getCapabilities;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LOD;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MIN_LOD;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL12;

import com.luminos.loaders.LuminosImage.Format;

/**
 * 
 * Loads images to GPU
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ImageLoader {
		
	protected ImageLoader() {
		
	}
	
	/**
	 * Loads buffered image to graphics card
	 * 
	 * @param bImage 		Buffered image describing the image
	 * @return 				Integer describing the image's location on the GPU
	 */	
	protected int loadTexture(BufferedImage bImage) {
		LuminosImage image = LuminosImage.loadImage(bImage, Format.RGBA);

		int textureID = glGenTextures();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.6f);
		
		if(getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float amount = Math.min(4f, glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		}
		
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
				
		Loader.textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Loads a PNG File to the graphics card
	 * 
	 * @param fileName		Location of the PNG file
	 * @return				Integer describing the location of the image on the GPU
	 * @throws IOException 	Exception for if file isn't found or cannot be handled
	 */
	protected int loadTexture(String fileName) throws IOException {
		LuminosImage image = LuminosImage.loadImage(fileName, Format.RGBA);

		int textureID = glGenTextures();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_LOD, -50.f);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_LOD, 5.0f);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -.6f);
		
		if(getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float amount = Math.min(4f, glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		}
		
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getBuffer());
						
		Loader.textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Loads cube map of PNG files to GPU
	 * 
	 * @param textureFiles		Array of strings pointing to files for the cube map
	 * @return 					Integer describing the cube map index on the graphics card
	 * @throws IOException 		Exception for if file isn't found or cannot be handled
	 */
	protected int loadCubeMap(String[] textureFiles) throws IOException {
		int textureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

		for (int i = 0; i < textureFiles.length; i++) {
			LuminosImage data = LuminosImage.loadImage(textureFiles[i], Format.RGBA);
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, data.getFormat().getGLImageFormat(), data.getWidth(), data.getHeight(), 0,
					GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		Loader.textures.add(textureID);
		return textureID;
	}

}
