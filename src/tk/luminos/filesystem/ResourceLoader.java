package tk.luminos.filesystem;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 
 * Loads resources packed into source folders,
 * rather than external files
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class ResourceLoader {
	
	/**
	 * Loads text from a resource to a string
	 * 
	 * @param file			File to load from
	 * @return				String containing source data
	 * @throws IOException	Thrown if resource cannot be found
	 */
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
	
	/**
	 * Loads resource to buffered image
	 * 
	 * @param file			File to load from
	 * @return				BufferedImage of file data
	 * @throws IOException	Thrown if file cannot be found
	 */
	public static BufferedImage loadImage(String file) throws IOException {
		URL url = Class.class.getResource(file);
		BufferedImage image = ImageIO.read(url);
		return image;
	}

}

