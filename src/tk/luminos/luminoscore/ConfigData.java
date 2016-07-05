package tk.luminos.luminoscore;

import tk.luminos.luminoscore.input.Keyboard;

public class ConfigData {
	
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
	
	public static Integer WATER_FBO_REFRAC_WIDTH = 1920;
	public static Integer WATER_FBO_REFRAC_HEIGHT = 1080;
	public static Integer WATER_FBO_REFLEC_WIDTH = 1920;
	public static Integer WATER_FBO_REFLEC_HEIGHT = 1080;
	
	public static Integer SIZE = 100;
	
	public static Integer TEXTURE_SIZE = 256;
	
	public static Integer POSITION = 0;
	public static Integer TEXTURES = 1;
	public static Integer NORMALS = 2;
	
	public static Integer FORWARD = Keyboard.KEY_W;
	public static Integer BACKWARD = Keyboard.KEY_S;
	public static Integer LEFT = Keyboard.KEY_A;
	public static Integer RIGHT = Keyboard.KEY_D;
	public static Integer SPRINT = Keyboard.KEY_LEFT_SHIFT;
	public static Integer WALK = Keyboard.KEY_LEFT_CONTROL;
	public static Integer JUMP = Keyboard.KEY_SPACE;
	
	public static final String FORMAT = "format";
	public static final String BOOLEAN = "bool";
	public static final String INT = "int";
	public static final String SHADER_LOC = "shader_loc";

}
