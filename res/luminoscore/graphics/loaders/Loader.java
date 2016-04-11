package luminoscore.graphics.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import luminoscore.Debug;
import luminoscore.graphics.models.RawModel;
import luminoscore.graphics.textures.TextureData;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Class that loads objects to the graphics card
 *
 */

public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param dimensions	int used to describe the number of dimensions [1, 4]
	 * @return	RawModel	RawModel containing GPU data on the vertices
	 * 
	 * Loads Positions to graphics card
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @return int			integer used to describe the index of the data on the GPU
	 * 
	 * Loads positions and texture coordinates to the graphics card
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	/**
	 * @param vertices		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param indices		integers used to describe the order of the vertices
	 * @return RawModel		RawModel containing GPU data on the vertices
	 * 
	 * Loads Positions, Texture Coordinates, and Indices to the graphics card
	 */
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, vertices.length / 3);
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords	floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param indices		integers used to describe the order of vertices
	 * @return RawModel		RawModel containing GPU data on the vertices.
	 * 
	 * Loads Positions, Texture Coordinates, Normals, and Indices to graphics card.  3D Coordinates.
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,
			int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * @param positions		floats used to describe the positions of the vertices
	 * @param textureCoords floats used to describe the texture coordinates of the vertices
	 * @param normals		floats used to describe the normal vectors of the vertices
	 * @param vertexCount	integer used to describe total number of vertices
	 * @return RawModel		RawModel containing GPU data on the vertices.
	 * 
	 * Loads Positions, Texture Coordinates, Normals, and Vertex Count to graphics card.  3D Coordinates.
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int vertexCount) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, vertexCount);
	}


	/**
	 * @param fileName 		PNG file
	 * @return int			integer describing the PNG index on the GPU
	 * 
	 * Loads PNG to graphics card
	 */
	public int loadTexture(String fileName) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			System.out.println("error");
		}

		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();

		int textureID = GL11.glGenTextures();
		
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1f);
		
		if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
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
	
	/**
	 * @param textureFiles		array of strings pointing to files for the cube map
	 * @return int				integer describing the cube map index on the graphics card
	 */
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/skybox/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	/**
	 * Deletes VAOs, Buffers, and Textures from GPU
	 */
	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}

//**********************************Private Methods**********************************//
	
	/**
	 * @return int	integer describing the index of a vertex array
	 * 
	 * Creates blank vertex array on graphics card
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	/**
	 * @param attributeNumber		integer describing where to store the data
	 * @param coordinateSize		integer describing the dimensions of the data [1, 4]
	 * @param data					floats describing the data to be put to the attribute
	 * 
	 * Stores float array in attribute of VAO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Unbinds Vertex Array
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	/**
	 * @param indices	integers describing the order of the vertices
	 * 
	 * Binds an element array buffer to the vertex array (VAO)
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	/**
	 * @param data			integers to be stored in an intbuffer
	 * @return	IntBuffer	IntBuffer of original data
	 * 
	 * Converts int array to intbuffer
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * @param data 			floats to be stored in a floatbuffer
	 * @return	FloatBuffer	FloatBuffer of original data
	 * 
	 * Converts float arry to floatbuffer
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * @param fileName			PNG file to be decoded
	 * @return TextureData		Texture Data holding the buffer from the file
	 * 
	 * Loads PNG file to TextureData for cube map
	 */
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Debug.addData(Loader.class + " Could not decode texture file: " + fileName);
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

}
