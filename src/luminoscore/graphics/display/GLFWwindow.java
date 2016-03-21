package luminoscore.graphics.display;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Toolkit;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import luminoscore.input.Keyboard;
import luminoscore.input.Mouse;
import luminoscore.input.MousePosition;

public class GLFWwindow {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	public static Integer GL_MAJOR = 3;
	public static Integer GL_MINOR = 0;
	
	//Callbacks
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback cursorPositionCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	
	//Inputs
	private Keyboard keyboard;
	private Mouse mouse;
	private MousePosition mousePosition;
	
	//Mouse Fields
	private float mouseX, mouseY, mouseDX, mouseDY;
	
	//FrameCounter Field
	private FrameCounter fc;
	
	//Constructor Fields
	private String title;
	private int width, height;
	private boolean fullscreen, vsync, resizable, mouseVisible, windowVisible;
	private long windowID;
	
	/*
	 * @param title Defines the title of the GLFW Window
	 * @param width Defines the width of the GLFW Window
	 * @param height Defines the height of the GLFW Window
	 * @param fullscreen Defines whether the GLFW Window is to be fullscreen
	 * @param vsync Defines whether the GLFW Window Frame Rate is to be constrained to VSync
	 * @param resizable Defines whether the GLFW Window may be resized
	 * @param mouseVisible Defines whether the mouse shall be visible in the GLFW Window
	 * @param windowVisible Defines whether the GLFW Window is to be visible
	 * 
	 * Constructor for the GLFW Window
	 * init() is to be called after constructor is defined
	 */
	public GLFWwindow(String title, int width, int height, boolean fullscreen,
			boolean vsync, boolean resizable, boolean mouseVisible,
			boolean windowVisible) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
		this.resizable = resizable;
		this.mouseVisible = mouseVisible;
		this.windowVisible = windowVisible;
		
		init();
	}
	
	//Creates GLFW Window and Instance from parameters defined in the constructor	
	private void init() {
		if(glfwInit() != GL_TRUE) {
			System.out.println("GLFW could not be initialized.");
		}
		
		//Set the window hints
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_MINOR);
		glfwWindowHint(GLFW_VISIBLE, windowVisible ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GL_TRUE : GL_FALSE);
		
		//Draw window
		if(!fullscreen) {
			windowID = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		} else {
			Toolkit tk = Toolkit.getDefaultToolkit();
			int w = (int) tk.getScreenSize().getWidth();
			int h = (int) tk.getScreenSize().getHeight();
			if(w != width || h != height) {
				System.err.println("Could not use user defined parameters: Width - " + width + " Height - " + height + ". "
						+ " Using system defined parameters: Width - " + w + " Height - " + h + ".");
			}
			windowID = glfwCreateWindow(w, h, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		}
		
		if(windowID == NULL) {
			System.out.println("GLFW Window could not be initialized.");
		}
		
		glfwMakeContextCurrent(windowID);
		
		//Create Callbacks
		errorCallback = GLFWErrorCallback.createPrint();
		errorCallback.set();
		
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			
			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
			}
			
		};
		
		framebufferSizeCallback.set(windowID);
		
		windowSizeCallback = new GLFWWindowSizeCallback() {
			
			public void invoke(long window, int width, int height) {
				GLFWwindow.this.width = width;
				GLFWwindow.this.height = height;
			}
			
		};
		
		windowSizeCallback.set(windowID);
		
		keyCallback = keyboard = new Keyboard();
		keyCallback.set(windowID);
		
		mouseButtonCallback = mouse = new Mouse();
		mouse.set(windowID);
		
		mouseX = mouseY = mouseDX = mouseDY = 0;
		
		glfwSetCursorPosCallback(windowID, cursorPositionCallback = mousePosition = new MousePosition() {
			
			public void invoke(long window, double xpos, double ypos) {
				
				mouseDX += xpos - mouseX;
				mouseDY += ypos - mouseY;
				mouseX = (float) xpos;
				mouseY = (float) ypos;
				
			}
			
		});
		
		mousePosition.set(windowID);
		
		glfwSwapInterval(vsync ? 1: 0);
		
		GL.createCapabilities();
		
		if(!mouseVisible) {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		} else {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
		
		fc = new FrameCounter();
		
	}
	
	//Clears the image off the screen manually.
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	//Updates the display.  Call after rendering to back buffer.
	public void update() {
		fc.start();
		glfwSwapBuffers(windowID);
		keyboard.update();
		glfwPollEvents();
		fc.calculate();
	}
	
	//Releases all callbacks.  Terminates GLFW instance.
	public void dispose() {
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPositionCallback.release();
		framebufferSizeCallback.release();
		windowSizeCallback.release();
		errorCallback.release();
		glfwTerminate();
	}
	
	//Getter methods for GLFWwindow
	public float getMouseX() {
		return mouseX;
	}

	public float getMouseY() {
		return mouseY;
	}

	public float getMouseDX() {
		return mouseDX;
	}

	public float getMouseDY() {
		return mouseDY;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public long getWindowID() {
		return windowID;
	}
	
	//Passed Getter Methods for FrameCounter
	public int getFPS() {
		return fc.getFPS();
	}
	
	public float getFrameTime() {
		return fc.getFrameTime();
	}

}

class FrameCounter {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Value Fields
	private int start, end, frameTime = 0, fps;
	private short frames = 0;
	private float frameTimeCount;
	
	//Constructor
	public FrameCounter() {}
	
	//Starts the FrameCounter.  Called at the beginning of the GLFWwindow's
	//update call.
	public void start() {
		start = (int) System.currentTimeMillis();
	}
	
	//Stops the FrameCounter and calculates the frametimes.  Called at the end
	//of the GLFWwindow's update call.
	public void calculate() {
		end = (int) System.currentTimeMillis();
		frameTime = frameTime + end - start;
		frameTimeCount = (end - start);
		frameTimeCount /= 1000;
		++frames;
		
		if(frameTime >= 1000) {
			fps = frames;
			frames = 0;
			frameTime = 0;
		}
	}
	
	public float getFrameTime() {
		return frameTimeCount;
	}
	
	public int getFPS() {
		return fps;
	}
	
}
