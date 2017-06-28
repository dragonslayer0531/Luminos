package tk.luminos.filesystem;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

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
	
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = ResourceLoader.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

}

