package tk.luminos.luminoscore;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
	
	public static String config_loc = "config.xml";

	protected long windowID;

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
		if(!GlobalLock.INITIATED) {
			GlobalLock.INITIATED = true;
		}

		//Check if file exists and initialize for loading
		File config = new File(config_loc);
		if(!config.exists()) {
			return new Luminos();
		}
		
		try {
			
			//Create and prepare document
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(config);
			doc.getDocumentElement().normalize();
			
			//Check to make sure config is up to date to version
			if(!doc.getDocumentElement().getNodeName().equals("luminos_config")) return new Luminos();
			Node version = doc.getElementById("version");
			if(version.getNodeValue().equals("1.0.0")) return new Luminos();
			
			//Load input methods
			NodeList input_methods = doc.getElementsByTagName("input_methods");
			for(int i = 0; i < input_methods.getLength(); i++) { 
				Element node = (Element) input_methods.item(i);
				GlobalLock.FORWARD = Integer.parseInt(node.getElementsByTagName("forward_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.BACKWARD = Integer.parseInt(node.getElementsByTagName("backward_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.LEFT = Integer.parseInt(node.getElementsByTagName("left_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.RIGHT = Integer.parseInt(node.getElementsByTagName("right_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.SPRINT = Integer.parseInt(node.getElementsByTagName("sprint_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.WALK = Integer.parseInt(node.getElementsByTagName("walk_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
				GlobalLock.JUMP = Integer.parseInt(node.getElementsByTagName("jump_key_binding").item(0).getAttributes().getNamedItem("key").getNodeValue());
			}

			//Load world data
			NodeList world = doc.getElementsByTagName("world");
			for(int i = 0; i < world.getLength(); i++) {
				Element node = (Element) world.item(i);
				GlobalLock.TEXTURE_SIZE = Integer.parseInt(node.getElementsByTagName("texture_size").item(0).getAttributes().getNamedItem("size").getNodeValue());
				GlobalLock.SIZE = Integer.parseInt(node.getElementsByTagName("chunk_size").item(0).getAttributes().getNamedItem("size").getNodeValue());
				GlobalLock.WATER_FBO_REFRAC_WIDTH = Integer.parseInt(node.getElementsByTagName("water_refrac").item(0).getAttributes().getNamedItem("width").getNodeValue());
				GlobalLock.WATER_FBO_REFRAC_HEIGHT = Integer.parseInt(node.getElementsByTagName("water_refrac").item(0).getAttributes().getNamedItem("height").getNodeValue());
				GlobalLock.WATER_FBO_REFLEC_WIDTH = Integer.parseInt(node.getElementsByTagName("water_reflec").item(0).getAttributes().getNamedItem("width").getNodeValue());
				GlobalLock.WATER_FBO_REFLEC_HEIGHT = Integer.parseInt(node.getElementsByTagName("water_reflec").item(0).getAttributes().getNamedItem("height").getNodeValue());
			}

			//Load window data
			NodeList window = doc.getElementsByTagName("window");
			for(int i = 0; i < window.getLength(); i++) {
				Element node = (Element) window.item(i);
				GlobalLock.WIDTH = Integer.parseInt(node.getElementsByTagName("dimensions").item(0).getAttributes().getNamedItem("width").getNodeValue());
				GlobalLock.HEIGHT = Integer.parseInt(node.getElementsByTagName("dimensions").item(0).getAttributes().getNamedItem("height").getNodeValue());
				GlobalLock.FULLSCREEN = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("fullscreen").getNodeValue());
				GlobalLock.MOUSE_VISIBLE = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("mouse_visible").getNodeValue());
				GlobalLock.RESIZABLE = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("resizable").getNodeValue());
				GlobalLock.VSYNC = Boolean.parseBoolean(node.getElementsByTagName("window_hints").item(0).getAttributes().getNamedItem("vsync").getNodeValue());
				GlobalLock.GL_MAJOR = Integer.parseInt(node.getElementsByTagName("gl_version").item(0).getAttributes().getNamedItem("major").getNodeValue());
				GlobalLock.GL_MINOR = Integer.parseInt(node.getElementsByTagName("gl_version").item(0).getAttributes().getNamedItem("minor").getNodeValue());
				GlobalLock.SAMPLES = Integer.parseInt(node.getElementsByTagName("msaa").item(0).getAttributes().getNamedItem("samples").getNodeValue());
				GlobalLock.STENCIL_BITS = Integer.parseInt(node.getElementsByTagName("msaa").item(0).getAttributes().getNamedItem("stencil_bits").getNodeValue());
			}
			
			//Load shader data
			NodeList shader = doc.getElementsByTagName("shader");
			for(int i = 0; i < shader.getLength(); i++) {
				Element node = (Element) shader.item(i);
				GlobalLock.POSITION = Integer.parseInt(node.getElementsByTagName("position").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
				GlobalLock.TEXTURES = Integer.parseInt(node.getElementsByTagName("texture").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
				GlobalLock.NORMALS = Integer.parseInt(node.getElementsByTagName("normal").item(0).getAttributes().getNamedItem("shader_loc").getNodeValue());
			}
			
			return new Luminos();

		} catch (Exception e) {
			Debug.addData(e.getMessage());
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
		GlobalLock.INITIATED = false;
		GlobalLock.printToFile("config.xml");
	}

}
