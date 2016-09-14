package tk.luminos;

import static tk.luminos.ConfigData.BACKWARD;
import static tk.luminos.ConfigData.FORWARD;
import static tk.luminos.ConfigData.FULLSCREEN;
import static tk.luminos.ConfigData.GL_MAJOR;
import static tk.luminos.ConfigData.GL_MINOR;
import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.INITIATED;
import static tk.luminos.ConfigData.JUMP;
import static tk.luminos.ConfigData.LEFT;
import static tk.luminos.ConfigData.MOUSE_VISIBLE;
import static tk.luminos.ConfigData.NORMALS;
import static tk.luminos.ConfigData.POSITION;
import static tk.luminos.ConfigData.RESIZABLE;
import static tk.luminos.ConfigData.RIGHT;
import static tk.luminos.ConfigData.SAMPLES;
import static tk.luminos.ConfigData.SCREENSHOT;
import static tk.luminos.ConfigData.SIZE;
import static tk.luminos.ConfigData.SPRINT;
import static tk.luminos.ConfigData.STENCIL_BITS;
import static tk.luminos.ConfigData.TEXTURES;
import static tk.luminos.ConfigData.TEXTURE_SIZE;
import static tk.luminos.ConfigData.VSYNC;
import static tk.luminos.ConfigData.WALK;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_WIDTH;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_WIDTH;
import static tk.luminos.ConfigData.WIDTH;
import static tk.luminos.GlobalLock.printToFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * Instantiates Luminos Instance.  Loads the engine's configuration data to the 
 * GlobalLock class for usage by other classes.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Luminos {
	
	public static String config_loc = "luminos.config";

	protected long windowID;
	
	public static List<Integer> vaos = new ArrayList<Integer>();
	public static List<Integer> vbos = new ArrayList<Integer>();
	public static List<Integer> textures = new ArrayList<Integer>();
	public static List<Integer> fbos = new ArrayList<Integer>();
	public static List<Integer> fboTextures = new ArrayList<Integer>();
	public static List<Integer> fboBuffers = new ArrayList<Integer>();
	public static List<Integer> audioSources = new ArrayList<Integer>();
	public static List<Integer> audioBuffers = new ArrayList<Integer>();

	/**
	 * Construction
	 */
	private Luminos() {}

	/**
	 * Invokes a new Luminos instance
	 * 
	 * @return		New Luminos Instance
	 */
	public static Luminos createLuminosInstance() {
		if(!INITIATED) {
			INITIATED = true;
		}

		//Check if file exists and initialize for loading
		File config = new File(config_loc);
		if(!config.exists()) {
			printToFile(config_loc);
		}
		
		try {
			
			//Create and prepare document
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(config);
			doc.getDocumentElement().normalize();
			
			//Check to make sure config is up to date to version
			if(!doc.getDocumentElement().getNodeName().equals("luminos_config")) {
				return new Luminos();
			}
			
			//Load input methods
			NodeList input_methods = doc.getElementsByTagName("input_methods");
			for(int i = 0; i < input_methods.getLength(); i++) { 
				Element node = (Element) input_methods.item(i);
				FORWARD = Integer.parseInt(node.getElementsByTagName("forward_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				BACKWARD = Integer.parseInt(node.getElementsByTagName("backward_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				LEFT = Integer.parseInt(node.getElementsByTagName("left_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				RIGHT = Integer.parseInt(node.getElementsByTagName("right_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				SPRINT = Integer.parseInt(node.getElementsByTagName("sprint_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				WALK = Integer.parseInt(node.getElementsByTagName("walk_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				JUMP = Integer.parseInt(node.getElementsByTagName("jump_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				SCREENSHOT = Integer.parseInt(node.getElementsByTagName("screenshot_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
			}

			//Load world data
			NodeList world = doc.getElementsByTagName("world");
			for(int i = 0; i < world.getLength(); i++) {
				Element node = (Element) world.item(i);
				TEXTURE_SIZE = Integer.parseInt(node.getElementsByTagName("texture_size").item(0).getAttributes().getNamedItem("size").getNodeValue());
				SIZE = Integer.parseInt(node.getElementsByTagName("chunk_size").item(0).getAttributes().getNamedItem("size").getNodeValue());
				WATER_FBO_REFRAC_WIDTH = Integer.parseInt(node.getElementsByTagName("water_refrac").item(0).getAttributes().getNamedItem("width").getNodeValue());
				WATER_FBO_REFRAC_HEIGHT = Integer.parseInt(node.getElementsByTagName("water_refrac").item(0).getAttributes().getNamedItem("height").getNodeValue());
				WATER_FBO_REFLEC_WIDTH = Integer.parseInt(node.getElementsByTagName("water_reflec").item(0).getAttributes().getNamedItem("width").getNodeValue());
				WATER_FBO_REFLEC_HEIGHT = Integer.parseInt(node.getElementsByTagName("water_reflec").item(0).getAttributes().getNamedItem("height").getNodeValue());
			}

			//Load window data
			NodeList window = doc.getElementsByTagName("window");
			for(int i = 0; i < window.getLength(); i++) {
				Element node = (Element) window.item(i);
				WIDTH = Integer.parseInt(node.getElementsByTagName("dimensions").item(0).getAttributes().getNamedItem("width").getNodeValue());
				HEIGHT = Integer.parseInt(node.getElementsByTagName("dimensions").item(0).getAttributes().getNamedItem("height").getNodeValue());
				FULLSCREEN = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("fullscreen").getNodeValue());
				MOUSE_VISIBLE = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("mouse_visible").getNodeValue());
				RESIZABLE = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("resizable").getNodeValue());
				VSYNC = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("vsync").getNodeValue());
				GL_MAJOR = Integer.parseInt(node.getElementsByTagName("gl_version").item(0).getAttributes().getNamedItem("major").getNodeValue());
				GL_MINOR = Integer.parseInt(node.getElementsByTagName("gl_version").item(0).getAttributes().getNamedItem("minor").getNodeValue());
				SAMPLES = Integer.parseInt(node.getElementsByTagName("msaa").item(0).getAttributes().getNamedItem("samples").getNodeValue());
				STENCIL_BITS = Integer.parseInt(node.getElementsByTagName("msaa").item(0).getAttributes().getNamedItem("stencil_bits").getNodeValue());
			}
			
			//Load shader data
			NodeList shader = doc.getElementsByTagName("shader");
			for(int i = 0; i < shader.getLength(); i++) {
				Element node = (Element) shader.item(i);
				POSITION = Integer.parseInt(node.getElementsByTagName("position").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
				TEXTURES = Integer.parseInt(node.getElementsByTagName("texture").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
				NORMALS = Integer.parseInt(node.getElementsByTagName("normal").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
			}
			
			return new Luminos();

		} catch (Exception e) {
			Debug.addData(e);
			return new Luminos();
		}
		
	}

	/**
	 * Sets the window ID
	 * 
	 * @param windowID		Window ID
	 */
	public void setWindowID(long windowID) {
		this.windowID = windowID;
	}

	/**
	 * Closes the instance
	 */
	public void close() {
		INITIATED = false;
		for(Integer vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		} for (Integer vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		} for (Integer texture : textures) {
			GL11.glDeleteTextures(texture);
		} for (Integer fbo : fbos) {
			GL30.glDeleteFramebuffers(fbo);
		} for (Integer fboTexture : fboTextures) {
			GL11.glDeleteTextures(fboTexture);
		} for (Integer fboBuffer : fboBuffers) {
			GL30.glDeleteRenderbuffers(fboBuffer);
		} for (Integer audioSource : audioSources) {
			AL10.alSourceStop(audioSource);
			AL10.alDeleteSources(audioSource);
		} for (Integer audioBuffer : audioBuffers) {
			AL10.alDeleteBuffers(audioBuffer);
		}
		printToFile(config_loc);
	}

}
