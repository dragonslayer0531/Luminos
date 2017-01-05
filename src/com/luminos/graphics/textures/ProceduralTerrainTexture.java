package com.luminos.graphics.textures;

import static com.luminos.ConfigData.SIZE;
import static com.luminos.ConfigData.TEXTURE_SIZE;

import java.awt.image.BufferedImage;

import com.luminos.graphics.terrains.Terrain;
import com.luminos.tools.maths.vector.Vector3f;
import com.luminos.tools.Maths;

/**
 * 
 * Creates the procedural texture
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ProceduralTerrainTexture {
	
	/**
	 * Generates a blend map for the texture
	 * 
	 * @param terrain	Terrain to create texture for
	 * @return			Blend map of terrain
	 */
	
	public static BufferedImage generateTerrainMap(Terrain terrain) {
		BufferedImage image = new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_RGB);
		
		/*
		 
		 32x32 HEIGHT MAP
		 HEIGHTMAP 0 = 1 IMAGE
		 HEIGHTMAP 31 = 256 IMAGE
		 
		 IMAGE = (HEIGHTMAP + 1) * (IMAGE.SIZE / HEIGHTMAP.SIZE)
		 
		 IF HEIGHT < 5:
		 	SET RGB TO SAND TEXTURE
		 IF HEIGHT > 10:
		 	SET RGB TO GRASS TEXTURE
		 IF HEIGHT <= 10 || HEIGHT >= 5
		 	COSINE INTERPOLATION FOR RGB VALUE
		 		INTERPOLATE EACH COMPONENT SEPARATELY
		 		
 		BLACK = GRASS
 		RED = SAND
		 
		 */
		
		int BLACK = Maths.rgbToInt(new Vector3f(0, 0, 0));
		int RED = Maths.rgbToInt(new Vector3f(255, 0, 0));
				
		for(int x = 0; x < TEXTURE_SIZE; x++) {
			for(int y = 0; y < TEXTURE_SIZE; y++) {
				float wX = terrain.getX();
				float wZ = terrain.getZ();
				
				float fracX = (x / (float) TEXTURE_SIZE) * SIZE;
				float fracZ = (y / (float) TEXTURE_SIZE) * SIZE;
				
				float worldX = wX + fracX;
				float worldZ = wZ + fracZ;
				
				float height = terrain.getHeightOfTerrain(worldX, worldZ);
				
				if(height > 5) {
					image.setRGB(x, y, BLACK);
				} else if (height > 0 && height <= 5) {
					float blend = (height / 5);
					int r = (int) Maths.CosineInterpolation(255, 0, blend);
					image.setRGB(x, y, Maths.rgbToInt(r, 0, 0));
				} else {
					image.setRGB(x, y, RED);
				}
			}
		}
		
		return image;
	}

}
