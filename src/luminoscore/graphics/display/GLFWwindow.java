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

public class GLFWWindow {
	
	/*
	 * Author: Nick Clark
	 * Created On: 12/26/15
	 * Last Updated: 12/29/15
	 * Update Info: Added documentation and mouse visibility.
	 */
	
	public static int GL_MAJOR = 3;
	public static int GL_MINOR = 2;
	
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
		
	/*
	 * Creates a GLFW window.  This should be the first OpenGL method called by the program.  Other classes, such as 
	 * entities and the physics classes need to be instantiated after the loading of the window.
	 * 
	 * @param title a string containing the title of the GLFW window
	 * @param width an int for how wide to make the window
	 * @param height an int for how tall to make the window
	 * @param vysnc a boolean determining whether to enable vertical sync
	 * @param fullscreen a boolean determining whether to make the GLFW window fullscreen.  If true, set the width and height to the screen size
	 * @param visible a boolean determining whether the GLFW window is visible
	 * @resizable a boolean determining whether the GLFW window can be resized along with the viewport
	 * @vismouse a boolean determining whether the mouse is visible while the GLFW window is active
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
	
	//Method that initializes the GLFW window defined by the constructor.
	
	private void init() {
		
		if(glfwInit() != GL_TRUE) {
			Debug.addData(GLFWWindow.class + " Could not instantiate GLFW instance");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_MINOR);
		glfwWindowHint(GLFW_VISIBLE, visible ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_STENCIL_BITS, 2);
		glfwWindowHint(GLFW_SAMPLES, 4);
		
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
		
		frameRateCounter = new FrameRateCounter(1);
	}
	
	//Clear Screen
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	//Update Screen
	
	public void update() {
		frameRateCounter.start();
		glfwSwapBuffers(window);
		keyboard.update();
		glfwPollEvents();
		frameRateCounter.calculate();
//		System.out.println(getFPS());
	}
	
	//Close screen
	
	public void dispose() {
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPosCallback.release();
		framebufferCallback.release();
		windowSizeCallback.release();
		glfwTerminate();
		errorCallback.release();
	}
	
	//Getter-Setter methods
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	public void close() {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}

	public GLFWErrorCallback getErrorCallback() {
		return errorCallback;
	}

	public void setErrorCallback(GLFWErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}

	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	public void setKeyCallback(GLFWKeyCallback keyCallback) {
		this.keyCallback = keyCallback;
	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}

	public void setMouseButtonCallback(GLFWMouseButtonCallback mouseButtonCallback) {
		this.mouseButtonCallback = mouseButtonCallback;
	}

	public GLFWCursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}

	public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
		this.cursorPosCallback = cursorPosCallback;
	}

	public GLFWFramebufferSizeCallback getFramebufferCallback() {
		return framebufferCallback;
	}

	public void setFramebufferCallback(
			GLFWFramebufferSizeCallback framebufferCallback) {
		this.framebufferCallback = framebufferCallback;
	}

	public GLFWWindowSizeCallback getWindowSizeCallback() {
		return windowSizeCallback;
	}

	public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback) {
		this.windowSizeCallback = windowSizeCallback;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(window, title);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		glfwSetWindowSize(window, width, height);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		glfwSetWindowSize(window, width, height);
	}

	public boolean isVsync() {
		return vsync;
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public long getWindow() {
		return window;
	}
	
	public FrameRateCounter getFPSCounter() {
		return frameRateCounter;
	}
	
	public float getFPS() {
		return this.frameRateCounter.getFPS();
	}
	
	public float getFrameTime() {
		return this.frameRateCounter.getFrameTime();
	}
	
	public float getDX() {
		float dx = mouseDX;
		mouseDX = 0;
		return dx;
	}
	
	public float getDY() {
		float dy = -mouseDY;
		mouseDY = 0;
		return dy;
	}
	
	public float getX() {
		return mouseX;
	}
	
	public float getY() {
		return mouseY;
	}
	
}
