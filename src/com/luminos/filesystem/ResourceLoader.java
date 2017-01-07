package com.luminos.filesystem;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

public class ResourceLoader {
	
	public static String loadText(String file) throws IOException {
		StringBuilder source = new StringBuilder();
		
		InputStream is = Class.class.getResourceAsStream(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		String line = "";
		
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#version"))
				source.append(line).append("//\n");
		}
		
		reader.close();
		
		return source.toString();
	}
	
	public static BufferedImage loadImage(String file) throws IOException {
		URL url = Class.class.getResource(file);
		BufferedImage image = ImageIO.read(url);
		return image;
	}

}

