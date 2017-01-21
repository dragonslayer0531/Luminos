package tk.luminos.graphics.display;

import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DOUBLEBUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwFocusWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;
import static tk.luminos.ConfigData.FULLSCREEN;
import static tk.luminos.ConfigData.HEIGHT;
import static tk.luminos.ConfigData.MOUSE_VISIBLE;
import static tk.luminos.ConfigData.RESIZABLE;
import static tk.luminos.ConfigData.VSYNC;
import static tk.luminos.ConfigData.WIDTH;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import tk.luminos.ConfigData;
import tk.luminos.Luminos;
import tk.luminos.filesystem.ResourceLoader;
import tk.luminos.graphics.render.GuiRenderer;
import tk.luminos.graphics.shaders.GuiShader;
import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.input.Keyboard;
import tk.luminos.input.Mouse;
import tk.luminos.input.MousePosition;
import tk.luminos.loaders.Loader;
import tk.luminos.maths.vector.Vector2f;

/**
 * 
 * The GLFWWindow class initializes GLFW and OpenGL contexts.
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class Window {

	//Set up callback information

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWFramebufferSizeCallback framebufferCallback;
	private GLFWWindowSizeCallback windowSizeCallback;

	private Keyboard keyboard;
	private Mouse mouse;
	private MousePosition mousePosition;

	private String title;
	private int width, height;
	private boolean vsync, fullscreen, visible = false, resizable, vismouse;
	private long window;
	private FrameRateCounter frameRateCounter;
	private float mouseX, mouseY, mouseDX, mouseDY;
	private Device device;
	
	/**
	 * Refresh rate of the {@link Device} displaying the window
	 */
	public static int REFRESH_RATE;

	/**
	 * Constructor that initiates the GLFW and OpenGL contexts, as well as the window itself
	 * 
	 * @param title 		Sets the GLFW Window's title
	 * @param width			Width of GLFW window
	 * @param height		Height of GLFW window
	 * @param vsync			Determines whether the window utilizes Vertical Synchronization
	 * @param fullscreen 	Determines whether the window and OpenGL Viewport is fullscreen.  Overrides the Width and Height if true
	 * @param resizable		Determines if the window is resizable
	 * @param vismouse		Determines if the mouse is visible when window is in focus
	 * @throws Exception	Thrown if GLFW cannot be initialized
	 */
	public Window(String title, int width, int height, boolean vsync, boolean fullscreen, boolean resizable, boolean vismouse) throws Exception {
		device = new Device();
		REFRESH_RATE = device.getRefreshRate();

		this.title = title;
		this.width = width;
		this.height = height;
		this.vsync = vsync;
		this.fullscreen = fullscreen;
		this.resizable = resizable;
		this.vismouse = vismouse;

		init();
	}

	/**
	 * Method that does full initialization of GLFW and OpenGL
	 * @throws Exception 
	 */
	private void init() throws Exception {

		errorCallback = new GLFWErrorCallback() {

			@Override
			public void invoke(int error, long description) {
				System.err.println("ERROR ID: " + error);
				System.err.println("DESCRIPTION: " + description);
				Luminos.exit(Luminos.EXIT_FAILURE);
			}
			
		};
		
		if(!glfwInit()) {
			throw new Exception("COULD NOT INSTANTIATE GLFW INSTANCE");
		}

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_REFRESH_RATE, REFRESH_RATE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

		if(fullscreen) {
			window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
		} else {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		}

		glfwMakeContextCurrent(window);

		//Create Callback

		framebufferCallback = new GLFWFramebufferSizeCallback() {

			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
			}

		};
		framebufferCallback.set(window);

		windowSizeCallback = new GLFWWindowSizeCallback() {

			public void invoke(long window, int width, int height) {
				Window.this.width = width;
				Window.this.height = height;
			}

		};
		windowSizeCallback.set(window);

		keyCallback = keyboard = new Keyboard();
		keyCallback.set(window);

		mouseButtonCallback = mouse = new Mouse();
		mouse.set(window);

		mouseX = mouseY = mouseDX = mouseDY = 0;

		glfwSetCursorPosCallback(window, cursorPosCallback = mousePosition = new MousePosition() {

			public void invoke(long window, double xpos, double ypos) {
				mouseDX += xpos - mouseX;
				mouseDY += ypos - mouseY;
				mouseX = (float) xpos;
				mouseY = (float) ypos;
			}

		});

		mousePosition.set(window);

		glfwSwapInterval(vsync ? 1 : 0);

		createCapabilities();
		
		glEnable(GL_MULTISAMPLE);

		if(!vismouse) {
			glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} 
		else 
			glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

		frameRateCounter = new FrameRateCounter();

		Loader loader = new Loader();
		GuiShader shader = new GuiShader();
		GuiRenderer gr = new GuiRenderer(shader, loader);
		BufferedImage image = ResourceLoader.loadImage("/logo.png");
		GUITexture logo = new GUITexture(loader.loadTexture(image), new Vector2f(0, 0), new Vector2f(1, 1));
		List<GUITexture> textures = new ArrayList<GUITexture>();
		textures.add(logo);
		gr.render(textures);
		gr.cleanUp();
		textures.clear();
		GLFW.glfwSwapBuffers(window);
		
		FULLSCREEN = fullscreen;
		VSYNC = vsync;
		RESIZABLE = resizable;
		MOUSE_VISIBLE = vismouse;
		
		ConfigData.WIDTH = width;
		ConfigData.HEIGHT = height;
	}

	/**
	 * Clears the OpenGL Color and Depth Buffers
	 */
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	/** 
	 * Updates the GLFW Window with the most recent buffer
	 * 
	 * @throws Exception		Thrown when method is called, but GLFW is not initialized
	 */
	public void update() throws Exception {
		frameRateCounter.start();
		keyboard.update();
		glfwSwapBuffers(window);
		glfwPollEvents();
		frameRateCounter.calculate();
	}

	/**
	 * Releases all callbacks and disposes of the window
	 */
	public void close() {
		keyCallback.free();
		mouseButtonCallback.free();
		cursorPosCallback.free();
		framebufferCallback.free();
		windowSizeCallback.free();
		errorCallback.free();
		glfwTerminate();
	}

	/**
	 * Getter of boolean deciding if the window should dispose
	 * 
	 * @return Value of whether the window should dispose or remain opened
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	/**
	 * Gets the GLFWWindow's Error Callback
	 * 
	 * @return Error Callback of the GLFW Instance
	 */
	public GLFWErrorCallback getErrorCallback() {
		return errorCallback;
	}

	/**
	 * Sets the GLFWWindow's Error Callback
	 * 
	 * @param errorCallback		GLFWErrorCallback to be used by the GLFW Instance
	 */
	public void setErrorCallback(GLFWErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}

	/**
	 * Gets the GLFWWindow's Key Callback
	 * 
	 * @return Key Callback of the GLFW Instance
	 */
	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	/**
	 * Sets the GLFWWindow's Key Callback
	 * 
	 * @param keyCallback 		GLFWKeyCallback to be used by the GLFW Instance
	 */
	public void setKeyCallback(GLFWKeyCallback keyCallback) {
		this.keyCallback = keyCallback;
	}

	/**
	 * Gets the GLFWWindow's Mouse Button Callback
	 * 
	 * @return Mouse Button Callback of the GLFW Instance
	 */
	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}

	/**
	 * Sets the GLFWWindow's Mouse Button Callback
	 * 
	 * @param mouseButtonCallback	GLFWMouseButtonCallback to be used by the GLFW Instance
	 */
	public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
		this.mouseButtonCallback = mouseButtonCallback;
	}

	/**
	 * Gets the GLFWWindow's Cursor Position Callback
	 * 
	 * @return Cursor Position Callback of the GLFW Instance
	 */
	public GLFWCursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}

	/**
	 * Sets the GLFWWindow's Cursor Position Callback
	 * 
	 * @param cursorPosCallback 	GLFWCursorPosCallback to be used by the GLFW Instance
	 */
	public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
		this.cursorPosCallback = cursorPosCallback;
	}

	/**
	 * Gets the GLFWWindow's Framebuffer Size Callback
	 * 
	 * @return Framebuffer Size Callback of the GLFW Instance
	 */
	public GLFWFramebufferSizeCallback getFramebufferCallback() {
		return framebufferCallback;
	}

	/**
	 * Sets the GLFWWindow's Framebuffer Size Callback	
	 * 
	 * @param framebufferCallback	GLFWFramebufferSizeCallback to be used by the GLFW Instance
	 */
	public void setFramebufferCallback(GLFWFramebufferSizeCallback framebufferCallback) {
		this.framebufferCallback = framebufferCallback;
	}

	/**
	 * Gets the GLFWWindow's Window Size Callback
	 * 
	 * @return Window Size Callback of the GLFW Instance
	 */
	public GLFWWindowSizeCallback getWindowSizeCallback() {
		return windowSizeCallback;
	}

	/**
	 * Sets the GLFWWindow's Window Size Callback
	 * 
	 * @param windowSizeCallback	GLFWWindowSizeCallback to be used by the GLFW Instance
	 */
	public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback) {
		this.windowSizeCallback = windowSizeCallback;
	}

	/**
	 * Gets the GLFWWindow's title
	 * 
	 * @return Title of the GLFWWindow instance
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the GLFWWindow's title
	 * 
	 * @param title			String to be used as title of the GLFW Instance
	 */
	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(window, title);
	}

	/**
	 * Gets the usage of VSync by the GLFWWindow
	 * 
	 * @return Value of the usage of Vertical Synchronization
	 */
	public boolean isVsync() {
		return vsync;
	}

	/**
	 * Gets the usage of Fullscreen by the GLFWWindow instance
	 * 
	 * @return Value of the usage of GLFWWindow fullscreen
	 */
	public boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Gets the visiblity of the GLFWWindow instance
	 * 
	 * @return Value of the usage of visibility
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Gets the ability to resize the GLFWWindow instance
	 * 
	 * @return Value of the ability to resize the GLFWWindow instance
	 */
	public boolean isResizable() {
		return resizable;
	}

	/**
	 * Gets the GLFWWindow ID
	 * 
	 * @return Value of the GLFWWindow Instance
	 */
	public long getWindow() {
		return window;
	}

	/**
	 * Gets the frames per second
	 * 
	 * @return Value of Frames per Second
	 */
	public float getFPS() {
		return this.frameRateCounter.getFPS();
	}

	/**
	 * Gets the frame time
	 * 
	 * @return Value of how long the last buffer occured
	 */
	public float getFrameTime() {
		return this.frameRateCounter.getFrameTime();
	}

	/**
	 * Gets the movement of the mouse along the X axis
	 * 
	 * @return Value of the delta x in the mouse movement
	 */
	public float getDX() {
		float dx = mouseDX;
		mouseDX = 0;
		return dx;
	}

	/**
	 * Gets the movement of the mouse along the Y axis
	 * 
	 * @return Value of the delta y in the mouse movement
	 */
	public float getDY() {
		float dy = -mouseDY;
		mouseDY = 0;
		return dy;
	}

	/**
	 * Gets the mouse's X coordinate
	 * 
	 * @return Value of the x position of the mouse
	 */
	public float getX() {
		return mouseX;
	}

	/**
	 * Gets the mouse's Y coordinate
	 * 
	 * @return Value of the y position of the mouse
	 */
	public float getY() {
		return mouseY;
	}

	/**
	 * Takes a screenshot of the front buffer
	 * 
	 * @param output			Output file location
	 * @param format			File format
	 * @throws IOException		Exception for not being able write to file
	 */
	public void takeScreenshot(String output, String format) throws IOException {
		glReadBuffer(GL_FRONT);
		int width = WIDTH;
		int height = HEIGHT;
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		File file = new File(output);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width; x++) 
		{
			for(int y = 0; y < height; y++)
			{
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		ImageIO.write(image, format, file);

	}
	
	public void printDeviceData() {
		System.out.println(device.toString());
	}
	
	public void showWindow() {
		glfwShowWindow(window);
		glfwFocusWindow(window);
	}

}

class FrameRateCounter {

	private double start;
	private double end;
	private double frameTime = 0;
	private short frames = 0;
	private double fps;

	/**
	 * Constructor of the frame rate counter
	 */
	public FrameRateCounter() {

	}

	/**
	 * Starts the frame rate counter.  Called at beginning of each GLFW update
	 * 
	 * @throws LuminosException		Checks if GLFW has been initialized
	 */
	public void start() throws Exception {
		if(glfwInit()) {
			start = GLFW.glfwGetTime();
		} else {
			throw new Exception("GLFW NOT INITIALISED");
		}
	}

	/**
	 * Ends the frame time counter and calculates.  Called at end of each GLFW update
	 * 
	 * @throws LuminosException		Checks if GLFW has been initialized
	 */
	public void calculate() throws Exception {
		if(glfwInit()) {
			end = GLFW.glfwGetTime();
		} else {
			throw new Exception("GLFW NOT INITIALISED");
		}
		frameTime = frameTime + end - start;
		++frames;
		if(frameTime >= (1)) {
			fps = frames;
			frames = 0;
			frameTime = 0;
		}

	}

	/**
	 * Gets the frames per second count
	 * 
	 * @return Value of FPS 
	 */
	public int getFPS() {
		return (int) fps;
	}

	/**
	 * Gets the length of time per frame
	 * 
	 * @return Value of seconds per frame
	 */
	public float getFrameTime() {
		return 1 / (float) Window.REFRESH_RATE;
	}

}
