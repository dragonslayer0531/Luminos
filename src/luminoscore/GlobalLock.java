package luminoscore;

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
	 * 
	 * Enumeration of Luminos Supported Graphics APIs
	 * 
	 * @author Nick Clark
	 * @version 1.0
	 *
	 */
	public enum GraphicsAPI {
		GLFW,
		VULKAN
	}
	
	public static Boolean INITIATED = false;
	
	public static final String VERSION = "1.0.0";
	
	public static GraphicsAPI API = GraphicsAPI.GLFW;
	public static Integer WIDTH = 1280;
	public static Integer HEIGHT = 720;
	public static boolean FULLSCREEN = false;
	public static boolean VSYNC = false;
	public static boolean RESIZABLE = false;
	public static boolean MOUSE_VISIBLE = false;
	public static Integer GL_MAJOR = 4;
	public static Integer GL_MINOR = 0;
	public static Integer STENCIL_BITS = 1;
	public static Integer SAMPLES = 4;
	
	public static Integer WATER_FBO_REFRAC_WIDTH = 640;
	public static Integer WATER_FBO_REFRAC_HEIGHT = 360;
	public static Integer WATER_FBO_REFLEC_WIDTH = 320;
	public static Integer WATER_FBO_REFLEC_HEIGHT = 180;
	
	public static Integer SIZE = 100;
	
	public static Integer TEXTURE_SIZE = 256;
	
	public static Integer POSITION = 0;
	public static Integer TEXTURES = 1;
	public static Integer NORMALS = 2;
	
	private static final String FORMAT = "format";
	private static final String BOOLEAN = "bool";
	private static final String INT = "int";
	private static final String SHADER_LOC = "shader_loc";
	
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
			Debug.addData(e.getMessage());
			return false;
		}
		
		if(db == null) {
			Debug.addData(GlobalLock.class.getName() + ": Could not create DocumentBuilder");
			return false;
		}
		Document doc = db.newDocument();
		
		Element root = doc.createElement("luminos_config");
		doc.appendChild(root);
		
		Element engine_data = doc.createElement("version");
		engine_data.appendChild(doc.createTextNode(VERSION));
		root.appendChild(engine_data);
		
		Element world = doc.createElement("world");
		root.appendChild(world);
		
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
			Debug.addData(e.getMessage());
			return false;
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file_loc));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			Debug.addData(e.getMessage());
			return false;
		}
		
		return new File(file_loc).exists();
	}

}
