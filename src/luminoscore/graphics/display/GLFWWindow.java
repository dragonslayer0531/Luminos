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

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

import luminoscore.Debug;
import luminoscore.GlobalLock;
import luminoscore.LuminosException;
import luminoscore.graphics.loaders.Loader;
import luminoscore.graphics.render.GuiRenderer;
import luminoscore.graphics.textures.GuiTexture;
import luminoscore.input.Keyboard;
import luminoscore.input.Mouse;
import luminoscore.input.MousePosition;

/**
 * 
 * @author Nick Clark
 * @version 1.1
 * 
 * The GLFWWindow class initializes GLFW and OpenGL contexts.
 *
 */

public class GLFWWindow {

	
	public static int GL_MAJOR = GlobalLock.GL_MAJOR;
	public static int GL_MINOR = GlobalLock.GL_MINOR;
	
	public static int STENCIL_BITS = GlobalLock.STENCIL_BITS;
	public static int SAMPLES = GlobalLock.SAMPLES;
	
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
	public GLFWWindow(String title, boolean vsync, boolean fullscreen, boolean visible, boolean resizable, boolean vismouse) throws LuminosException {
		this.title = title;
		this.width = GlobalLock.WIDTH;
		this.height = GlobalLock.HEIGHT;
		this.vsync = vsync;
		this.fullscreen = fullscreen;
		this.visible = visible;
		this.resizable = resizable;
		this.vismouse = vismouse;
		
		init();
	}
	
	/**
	 * Method that does full initialization of GLFW and OpenGL
	 * @throws LuminosException 
	 */
	private void init() throws LuminosException {
		
		if(!GlobalLock.INITIATED) {
			throw new LuminosException(GLFWWindow.class + " Luminos Engine has not been instantiated.  Closing...");
		}
		
		if(glfwInit() != GL_TRUE) {
			Debug.addData(GLFWWindow.class + " Could not instantiate GLFW instance");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_MINOR);
		glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_STENCIL_BITS, STENCIL_BITS);
		glfwWindowHint(GLFW_SAMPLES, SAMPLES);
		
		if(fullscreen) {
			window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
		} else {
			window = glfwCreateWindow(width, height, title, GL_FALSE, NULL);
		}
		
		if(window == NULL) {
			Debug.addData(GLFWWindow.class + " Could not create GLFW Window");
			Debug.print();
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
		
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		if(!vismouse) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} else GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		
		frameRateCounter = new FrameRateCounter();
				
		Loader loader = new Loader();
		GuiRenderer gr = new GuiRenderer(loader);
		GuiTexture logo = new GuiTexture(loader.loadTexture("res/textures/logo.png"), new Vector2f(0, 0), new Vector2f(1, 1));
		List<GuiTexture> textures = new ArrayList<GuiTexture>();
		textures.add(logo);
		gr.render(textures);
		gr.cleanUp();
		textures.clear();
		loader.cleanUp();
		GLFW.glfwSwapBuffers(window);
		
		GlobalLock.FULLSCREEN = fullscreen;
		GlobalLock.VSYNC = vsync;
		GlobalLock.RESIZABLE = resizable;
		GlobalLock.MOUSE_VISIBLE = vismouse;
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
		keyboard.update();
		glfwSwapBuffers(window);
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
	 * Getter of boolean deciding if the window should close
	 * 
	 * @return Value of whether the window should close or remain opened
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
	
}
