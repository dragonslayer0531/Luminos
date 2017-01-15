package tk.luminos;

import tk.luminos.input.Keyboard;

public class ConfigData {
		
	public static final String VERSION = "0.0.1a";
	
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
	
	public static Long SEED = 0l;
	
	public static Integer UPS = 60;
	public static Integer FPS = 60;
	
	public static Integer WATER_FBO_REFRAC_WIDTH = 1920;
	public static Integer WATER_FBO_REFRAC_HEIGHT = 1080;
	public static Integer WATER_FBO_REFLEC_WIDTH = 1920;
	public static Integer WATER_FBO_REFLEC_HEIGHT = 1080;
	
	public static boolean POSTPROCESS = true;
	
	public static Integer SIZE = 100;
	public static Integer TEXTURE_SIZE = 256;
	
	public static Integer POSITION = 0;
	public static Integer TEXTURES = 1;
	public static Integer NORMALS = 2;
	
	public static Integer SCREENSHOT = Keyboard.KEY_F12;
	
	public static final String FORMAT = "format";
	public static final String BOOLEAN = "bool";
	public static final String INT = "int";
	public static final String SHADER_LOC = "shader_loc";
	
}
