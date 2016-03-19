package luminoscore.graphics.terrains;

import java.awt.image.BufferedImage;

public class NormalMap {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/18/2016
	 */
	
	//Constructor Field
	private float[] normals;
	
	/*
	 * @param normals Float array of normals containing the normal map
	 * 
	 * Constructor
	 */
	public NormalMap(float[] normals) {
		this.normals = normals;
	}
	
	public BufferedImage draw() {
		int size = (int) Math.sqrt(normals.length / 3);
		int row = 0, column = 0;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < normals.length/3 - 1; i++) {
			int r = (int) normals[i] * 255;
			int g = (int) normals[i + 1] * 255;
			int b = (int) normals[i + 2] * 255;
			int color = (r << 16) | (g << 8) | b;
			if(column == 16) {
				column = 0;
				row++;
			}
			image.setRGB(column, row, color);
			column++;
		}
		return image;
	}

}

