package luminoscore.graphics.display;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_STENCIL_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import luminoscore.Debug;
import luminoscore.input.Keyboard;
import luminoscore.input.Mouse;
import luminoscore.input.MousePosition;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * The GLFWWindow class initializes GLFW and OpenGL contexts.
 *
 */

public class GLFWWindow {

	
	public static int GL_MAJOR = 3;
	public static int GL_MINOR = 2;
	
	public static int STENCIL_BITS = 2;
	public static int SAMPLES = 4;
	
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
	private boolean vsync, fullscreen, visible, resizable, vismouse;
	private long window;
	private FrameRateCounter frameRateCounter;
	
	private float mouseX, mouseY, mouseDX, mouseDY;
		
	/**
	 * 
	 * @param title 		Sets the GLFW Window's title
	 * @param width 		Sets the width value of the window and of the OpenGL Viewport
	 * @param height	   	Sets the height value of the window and of the OpenGL Viewport
	 * @param vsync			Determines whether the window utilizes Vertical Synchronization
	 * @param fullscreen 	Determines whether the window and OpenGL Viewport is fullscreen.  
	 * 							Overrides the Width and Height if true
	 * @param visible		Determines if the window is visible on load
	 * @param resizable		Determines if the window is resizable
	 * @param vismouse		Determines if the mouse is visible when window is in focus
	 * 
	 * Constructor that initiates the GLFW and OpenGL contexts, as well as the window itself
	 */
	public GLFWWindow(String title, int width, int height, boolean vsync, boolean fullscreen, boolean visible, boolean resizable, boolean vismouse) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vsync = vsync;
		this.fullscreen = fullscreen;
		this.visible = visible;
		this.resizable = resizable;
		this.vismouse = vismouse;
		
		init();
	}
	
	/**
	 * Method that does full initialization of GLFW and OpenGL
	 */
	private void init() {
		
		if(glfwInit() != GL_TRUE) {
			Debug.addData(GLFWWindow.class + " Could not instantiate GLFW instance");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_MINOR);
		glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_STENCIL_BITS, STENCIL_BITS);
		glfwWindowHint(GLFW_SAMPLES, SAMPLES);
		
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		if(window == NULL) {
			Debug.addData(GLFWWindow.class + "Could not create GLFW Window");
		}
			
		glfwMakeContextCurrent(window);
		
		//Create Callbacks
		
		errorCallback = GLFWErrorCallback.createPrint();
		errorCallback.set();
				
		framebufferCallback = new GLFWFramebufferSizeCallback() {

			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
			}
			
		};
		framebufferCallback.set(window);
		
		windowSizeCallback = new GLFWWindowSizeCallback() {
			
			public void invoke(long window, int width, int height) {
				GLFWWindow.this.width = width;
				GLFWWindow.this.height = height;
			}
			
		};
		windowSizeCallback.set(window);
		
		keyCallback = keyboard = new Keyboard();
		keyCallback.set(window);
		
		mouseButtonCallback = mouse = new Mouse();
		mouse.set(window);
		
		mouseX = mouseY = mouseDX = mouseDY = 0;
		
		GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = mousePosition = new MousePosition() {
			
			public void invoke(long window, double xpos, double ypos) {
				mouseDX += xpos - mouseX;
				mouseDY += ypos - mouseY;
				mouseX = (float) xpos;
				mouseY = (float) ypos;
			}
			
		});
		
		mousePosition.set(window);
		
		glfwSwapInterval(vsync ? 1 : 0);
		
		GL.createCapabilities();

		if(!vismouse) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} else GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		
		frameRateCounter = new FrameRateCounter();
	}
	
	/**
	 * Clears the OpenGL Color and Depth Buffers
	 */
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/** 
	 * Updates the GLFW Window with the most recent buffer
	 */
	public void update() {
		frameRateCounter.start();
		glfwSwapBuffers(window);
		keyboard.update();
		glfwPollEvents();
		frameRateCounter.calculate();
	}
	
	/**
	 * Releases all callbacks and disposes of the window
	 */
	public void dispose() {
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPosCallback.release();
		framebufferCallback.release();
		windowSizeCallback.release();
		glfwTerminate();
		errorCallback.release();
	}
	
	/**
	 * @return boolean		Value of whether the window should close or remain opened
	 * 
	 * Getter of boolean deciding if the window should close
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	/**
	 * Sets the window close to true
	 */
	public void close() {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}

	/**
	 * @return GLFWErrorCallback	Error Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Error Callback
	 */
	public GLFWErrorCallback getErrorCallback() {
		return errorCallback;
	}

	/**
	 * @param errorCallback		GLFWErrorCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Error Callback
	 */
	public void setErrorCallback(GLFWErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}

	/**
	 * @return GLFWKeyCallback	Key Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Key Callback
	 */
	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	/**
	 * @param keyCallback 		GLFWKeyCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Key Callback
	 */
	public void setKeyCallback(GLFWKeyCallback keyCallback) {
		this.keyCallback = keyCallback;
	}

	/**
	 * @return GLFWMouseButtonCallback	Mouse Button Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Mouse Button Callback
	 */
	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}

	/**
	 * @param mouseButtonCallback	GLFWMouseButtonCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Mouse Button Callback
	 */
	public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
		this.mouseButtonCallback = mouseButtonCallback;
	}

	/**
	 * @return GLFWCursorPosCallback	Cursor Position Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Cursor Position Callback
	 */
	public GLFWCursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}

	/**
	 * @param cursorPosCallback 	GLFWCursorPosCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Cursor Position Callback
	 */
	public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
		this.cursorPosCallback = cursorPosCallback;
	}

	/**
	 * @return GLFWFramebufferSizeCallback	Framebuffer Size Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Framebuffer Size Callback
	 */
	public GLFWFramebufferSizeCallback getFramebufferCallback() {
		return framebufferCallback;
	}

	/**
	 * @param framebufferCallback	GLFWFramebufferSizeCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Framebuffer Size Callback	
	 */
	public void setFramebufferCallback(GLFWFramebufferSizeCallback framebufferCallback) {
		this.framebufferCallback = framebufferCallback;
	}

	/**
	 * @return GLFWWindowSizeCallback	Window Size Callback of the GLFW Instance
	 * 
	 * Gets the GLFWWindow's Window Size Callback
	 */
	public GLFWWindowSizeCallback getWindowSizeCallback() {
		return windowSizeCallback;
	}

	/**
	 * @param windowSizeCallback	GLFWWindowSizeCallback to be used by the GLFW Instance
	 * 
	 * Sets the GLFWWindow's Window Size Callback
	 */
	public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback) {
		this.windowSizeCallback = windowSizeCallback;
	}

	/**
	 * @return String		Title of the GLFWWindow instance
	 * 
	 * Gets the GLFWWindow's title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title			String to be used as title of the GLFW Instance
	 * 
	 * Sets the GLFWWindow's title
	 */
	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(window, title);
	}

	/**
	 * @return int			Width of the GLFWWindow instance
	 * 
	 * Gets the GLFWWindow's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width			Integer to be used as the width of the GLFWWindow instance
	 * 
	 * Sets the GLFWWindow's width
	 */
	public void setWidth(int width) {
		this.width = width;
		glfwSetWindowSize(window, width, height);
	}

	/**
	 * @return int 			Height of the GLFWWindow instance
	 * 
	 * Gets the GLFWWindow's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height		Integer to be used as the height of the GLFWWindow instance
	 * 
	 * Sets the GLFWWindow's height
	 */
	public void setHeight(int height) {
		this.height = height;
		glfwSetWindowSize(window, width, height);
	}

	/**
	 * @return boolean		Value of the usage of Vertical Synchronization
	 * 
	 * Gets the usage of VSync by the GLFWWindow
	 */
	public boolean isVsync() {
		return vsync;
	}

	/**
	 * @return boolean		Value of the usage of GLFWWindow fullscreen
	 * 
	 * Gets the usage of Fullscreen by the GLFWWindow instance
	 */
	public boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * @return boolean 		Value of the usage of visibility
	 * 
	 * Gets the visiblity of the GLFWWindow instance
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @return boolean 		Value of the ability to resize the GLFWWindow instance
	 * 
	 * Gets the ability to resize the GLFWWindow instance
	 */
	public boolean isResizable() {
		return resizable;
	}

	/**
	 * @return long 		Value of the GLFWWindow Instance
	 * 
	 * Gets the GLFWWindow ID
	 */
	public long getWindow() {
		return window;
	}
	
	/**
	 * @return float		Value of Frames per Second
	 * 
	 * Gets the frames per second
	 */
	public float getFPS() {
		return this.frameRateCounter.getFPS();
	}
	
	/**
	 * @return float 		Value of how long the last buffer occured
	 * 
	 * Gets the frame time
	 */
	public float getFrameTime() {
		return this.frameRateCounter.getFrameTime();
	}
	
	/**
	 * @return float 		Value of the delta x in the mouse movement
	 * 
	 * Gets the movement of the mouse along the X axis
	 */
	public float getDX() {
		float dx = mouseDX;
		mouseDX = 0;
		return dx;
	}
	
	/**
	 * @return float 		Value of the delta y in the mouse movement
	 * 
	 * Gets the movement of the mouse along the Y axis
	 */
	public float getDY() {
		float dy = -mouseDY;
		mouseDY = 0;
		return dy;
	}
	
	/**
	 * @return float 		Value of the x position of the mouse
	 * 
	 * Gets the mouse's X coordinate
	 */
	public float getX() {
		return mouseX;
	}
	
	/**
	 * @return float 		Value of the y position of the mouse
	 * 
	 * Gets the mouse's Y coordinate
	 */
	public float getY() {
		return mouseY;
	}
	
}
