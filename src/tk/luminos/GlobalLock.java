package tk.luminos;

import static tk.luminos.ConfigData.API;
import static tk.luminos.ConfigData.BACKWARD;
import static tk.luminos.ConfigData.BOOLEAN;
import static tk.luminos.ConfigData.FORMAT;
import static tk.luminos.ConfigData.FORWARD;
import static tk.luminos.ConfigData.FULLSCREEN;
import static tk.luminos.ConfigData.GL_MAJOR;
import static tk.luminos.ConfigData.GL_MINOR;
import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.INT;
import static tk.luminos.ConfigData.JUMP;
import static tk.luminos.ConfigData.LEFT;
import static tk.luminos.ConfigData.MOUSE_VISIBLE;
import static tk.luminos.ConfigData.NORMALS;
import static tk.luminos.ConfigData.POSITION;
import static tk.luminos.ConfigData.RESIZABLE;
import static tk.luminos.ConfigData.RIGHT;
import static tk.luminos.ConfigData.SAMPLES;
import static tk.luminos.ConfigData.SHADER_LOC;
import static tk.luminos.ConfigData.SIZE;
import static tk.luminos.ConfigData.SPRINT;
import static tk.luminos.ConfigData.STENCIL_BITS;
import static tk.luminos.ConfigData.TEXTURES;
import static tk.luminos.ConfigData.TEXTURE_SIZE;
import static tk.luminos.ConfigData.VERSION;
import static tk.luminos.ConfigData.VSYNC;
import static tk.luminos.ConfigData.WALK;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFLEC_WIDTH;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_HEIGHT;
import static tk.luminos.ConfigData.WATER_FBO_REFRAC_WIDTH;
import static tk.luminos.ConfigData.WIDTH;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * Holds global data for loading to the configuration
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GlobalLock {
	
	/**
	 * Prints the data to an XML file
	 * 
	 * @param file_loc		Where to print file to
	 * @return				If the file printed correctly
	 */
	
	public static boolean printToFile(String file_loc) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Debug.addData(e);
			return false;
		}
		
		if(db == null) {
			Debug.addData(new LuminosException("COULD NOT CREATE DOCUMENT BUILDER"));
			return false;
		}
		Document doc = db.newDocument();
		
		Element root = doc.createElement("luminos_config");
		doc.appendChild(root);
		
		Element engine_data = doc.createElement("version");
		engine_data.appendChild(doc.createTextNode(VERSION));
		root.appendChild(engine_data);
		
		Element input = doc.createElement("input_methods");
		root.appendChild(input);
		
		Element world = doc.createElement("world");
		root.appendChild(world);
		
		Element forward = doc.createElement("forward_key_binding");
		forward.setAttribute(FORMAT, INT);
		forward.setAttribute("key", FORWARD.toString());
		input.appendChild(forward);
		
		Element backward = doc.createElement("backward_key_binding");
		backward.setAttribute(FORMAT, INT);
		backward.setAttribute("key", BACKWARD.toString());
		input.appendChild(backward);
		
		Element left = doc.createElement("left_key_binding");
		left.setAttribute(FORMAT, INT);
		left.setAttribute("key", LEFT.toString());
		input.appendChild(left);
		
		Element right = doc.createElement("right_key_binding");
		right.setAttribute(FORMAT, INT);
		right.setAttribute("key", RIGHT.toString());
		input.appendChild(right);
		
		Element sprint = doc.createElement("sprint_key_binding");
		sprint.setAttribute(FORMAT, INT);
		sprint.setAttribute("key", SPRINT.toString());
		input.appendChild(sprint);
		
		Element walk = doc.createElement("walk_key_binding");
		walk.setAttribute(FORMAT, INT);
		walk.setAttribute("key", WALK.toString());
		input.appendChild(walk);
		
		Element jump = doc.createElement("jump_key_binding");
		jump.setAttribute(FORMAT, INT);
		jump.setAttribute("key", JUMP.toString());
		input.appendChild(jump);
		
		Element screenshot = doc.createElement("screenshot_key_binding");
		screenshot.setAttribute(FORMAT, INT);
		screenshot.setAttribute("key", JUMP.toString());
		input.appendChild(screenshot);
		
		Element textureSize = doc.createElement("texture_size");
		textureSize.setAttribute(FORMAT, INT);
		textureSize.setAttribute("size", TEXTURE_SIZE.toString());
		world.appendChild(textureSize);
		
		Element chunkSize = doc.createElement("chunk_size");
		chunkSize.setAttribute(FORMAT, INT);
		chunkSize.setAttribute("size", SIZE.toString());
		world.appendChild(chunkSize);
		
		Element waterRefrac = doc.createElement("water_refrac");
		waterRefrac.setAttribute(FORMAT, INT);
		waterRefrac.setAttribute("width", WATER_FBO_REFRAC_WIDTH.toString());
		waterRefrac.setAttribute("height", WATER_FBO_REFRAC_HEIGHT.toString());
		world.appendChild(waterRefrac);
		
		Element waterReflec = doc.createElement("water_reflec");
		waterReflec.setAttribute(FORMAT, INT);
		waterReflec.setAttribute("width", WATER_FBO_REFLEC_WIDTH.toString());
		waterReflec.setAttribute("height", WATER_FBO_REFLEC_HEIGHT.toString());
		world.appendChild(waterReflec);
		
		Element window = doc.createElement("window");
		window.setAttribute("api", API.name());
		root.appendChild(window);
		
		Element dimensions = doc.createElement("dimensions");
		dimensions.setAttribute(FORMAT, INT);
		dimensions.setAttribute("width", WIDTH.toString());
		dimensions.setAttribute("height", HEIGHT.toString());
		window.appendChild(dimensions);
		
		Element windowHints = doc.createElement("window_hints");
		windowHints.setAttribute(FORMAT, BOOLEAN);
		windowHints.setAttribute("fullscreen", Boolean.toString(FULLSCREEN));
		windowHints.setAttribute("vsync", Boolean.toString(VSYNC));
		windowHints.setAttribute("resizable", Boolean.toString(RESIZABLE));
		windowHints.setAttribute("mouse_visible", Boolean.toString(MOUSE_VISIBLE));
		window.appendChild(windowHints);
		
		Element glVersion = doc.createElement("gl_version");
		glVersion.setAttribute(FORMAT, INT);
		glVersion.setAttribute("major", GL_MAJOR.toString());
		glVersion.setAttribute("minor", GL_MINOR.toString());
		window.appendChild(glVersion);
		
		Element antialiasing = doc.createElement("msaa");
		antialiasing.setAttribute(FORMAT, INT);
		antialiasing.setAttribute("stencil_bits", STENCIL_BITS.toString());
		antialiasing.setAttribute("samples", SAMPLES.toString());
		window.appendChild(antialiasing);
		
		Element shader = doc.createElement("shader");
		root.appendChild(shader);
		
		Element position = doc.createElement("position");
		position.setAttribute(FORMAT, INT);
		position.setAttribute(SHADER_LOC, POSITION.toString());
		shader.appendChild(position);
		
		Element texture = doc.createElement("texture");
		texture.setAttribute(FORMAT, INT);
		texture.setAttribute(SHADER_LOC, TEXTURES.toString());
		shader.appendChild(texture);
		
		Element normal = doc.createElement("normal");
		normal.setAttribute(FORMAT, INT);
		normal.setAttribute(SHADER_LOC, NORMALS.toString());
		shader.appendChild(normal);
		
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			Debug.addData(e);
			return false;
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file_loc));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			Debug.addData(e);
			return false;
		}
		
		return new File(file_loc).exists();
	}

}
