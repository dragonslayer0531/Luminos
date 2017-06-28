package tk.luminos.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import tk.luminos.Application;
import tk.luminos.Luminos;
import tk.luminos.filesystem.ResourceLoader;
import tk.luminos.graphics.SceneManager;
import tk.luminos.graphics.render.ImageRenderer;
import tk.luminos.graphics.render.SceneRenderer;
import tk.luminos.graphics.ui.NuklearContext;
import tk.luminos.input.Keyboard;
import tk.luminos.input.Mouse;
import tk.luminos.loaders.AssetCache;
import tk.luminos.loaders.Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static tk.luminos.Engine.ERROR_STREAM;
import static tk.luminos.Luminos.ExitStatus.FAILURE_GENERAL;

/**
 * 
 * The GLFWWindow class initializes GLFW and OpenGL contexts.
 * 
 * @author Nick Clark
 * @version 1.1
 *
 */

public class Window {

	public static Float ASPECT_RATIO = 16.0f / 9.0f;

	//Set up callback information

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWFramebufferSizeCallback framebufferCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWCursorPosCallback cursorPosCallback;

	private String title;
	private int width, height;
	private boolean vsync, fullscreen, visible = false, vismouse;
	private long window;
	private FrameRateCounter frameRateCounter;

	private static Window instance;

	/**
	 * Refresh rate of the {@link Device} displaying the window
	 */
	public static int REFRESH_RATE = 60;

	/**
	 * Creates a new GLFW window
	 * 
	 * @param title 		Sets the GLFW Window's title
	 * @param width			Width of GLFW window
	 * @param height		Height of GLFW window
	 * @param vsync			Determines whether the window utilizes Vertical Synchronization
	 * @param fullscreen 	Determines whether the window and OpenGL Viewport is fullscreen.  Overrides the Width and Height if true
	 * @param vismouse		Determines if the mouse is visible when window is in focus
	 * @return				New global GLFW window
	 * @throws Exception	Thrown if GLFW cannot be initialized
	 */
	public static Window create(String title, int width, int height, boolean vsync, boolean fullscreen, boolean vismouse) throws Exception {
		if (instance != null) 
			throw new RuntimeException("Window already initialized!");
		instance = new Window(title, width, height, vsync, fullscreen, vismouse);
		NuklearContext.create();
		return instance;
	}

	/**
	 * Gets the current global instance of the Window
	 * 
	 * @return				Global GLFW window instance
	 */
	public static Window getInstance() {
		if (instance == null)
			throw new NullPointerException("Window is not initialized");
		return instance;
	}

	/**
	 * Constructor that initiates the GLFW and OpenGL contexts, as well as the window itself
	 * 
	 * @param title 		Sets the GLFW Window's title
	 * @param width			Width of GLFW window
	 * @param height		Height of GLFW window
	 * @param vsync			Determines whether the window utilizes Vertical Synchronization
	 * @param fullscreen 	Determines whether the window and OpenGL Viewport is fullscreen.  Overrides the Width and Height if true
	 * @param vismouse		Determines if the mouse is visible when window is in focus
	 * @throws Exception	Thrown if GLFW cannot be initialized
	 */
	private Window(String title, int width, int height, boolean vsync, boolean fullscreen, boolean vismouse) throws Exception {
		this.title = title;
		this.width = width;
		this.height = height;
		ASPECT_RATIO = (float) width / (float) height;
		this.vsync = vsync;
		this.fullscreen = fullscreen;
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
				ERROR_STREAM.append(error + ": " + description);
				Luminos.exit(FAILURE_GENERAL);
			}

		};
		glfwSetErrorCallback(errorCallback);

		if(!glfwInit()) {
			throw new Exception("COULD NOT INSTANTIATE GLFW INSTANCE");
		}

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		if (Platform.get() == Platform.MACOSX)
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		if (Application.getValue("DEBUG") == 1)
			glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		if(fullscreen) {
			GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwWindowHint(GLFW_RED_BITS, mode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
			glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
			window = glfwCreateWindow(width, height, title, NULL, NULL);
			width = mode.width();
			height = mode.height();
			glfwSetWindowMonitor(window, NULL, 0, 0, mode.width(), mode.height(), mode.refreshRate());
		} else {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		}
		
		Application.setValue("FULLSCREEN", fullscreen ? 1 : 0);
		Application.setValue("VSYNC", vsync ? 1 : 0);
		Application.setValue("MOUSE_VISIBLE", vismouse ? 1 : 0);

		Application.setValue("WIDTH", width);
		Application.setValue("HEIGHT", height);

		if (window == NULL)
			throw new RuntimeException("Failed to create window!");

		glfwMakeContextCurrent(window);

		//Create Callback

		glfwSetFramebufferSizeCallback(window, framebufferCallback = new GLFWFramebufferSizeCallback() {

			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
				ASPECT_RATIO = (float) width / (float) height;
			}

		});

		glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int width, int height) {
				Application.setValue("WIDTH", width);
				Application.setValue("HEIGHT", height);
				glViewport(0, 0, width, height);
				ASPECT_RATIO = (float) width / (float) height;
                Window.this.width = width;
                Window.this.height = height;
                SceneRenderer.resize();
                SceneManager.resize();
            }
		});

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				Keyboard.getInstance().update(key, action);

				boolean press = action == GLFW_PRESS;
				NkContext ctx = NuklearContext.getInstance().ctx;
				switch (key) {
				case GLFW_KEY_DELETE:
					nk_input_key(ctx, NK_KEY_DEL, press);
					break;
				case GLFW_KEY_ENTER:
					nk_input_key(ctx, NK_KEY_ENTER, press);
					break;
				case GLFW_KEY_TAB:
					nk_input_key(ctx, NK_KEY_TAB, press);
					break;
				case GLFW_KEY_BACKSPACE:
					nk_input_key(ctx, NK_KEY_BACKSPACE, press);
					break;
				case GLFW_KEY_UP:
					nk_input_key(ctx, NK_KEY_UP, press);
					break;
				case GLFW_KEY_DOWN:
					nk_input_key(ctx, NK_KEY_DOWN, press);
					break;
				case GLFW_KEY_HOME:
					nk_input_key(ctx, NK_KEY_TEXT_START, press);
					nk_input_key(ctx, NK_KEY_SCROLL_START, press);
					break;
				case GLFW_KEY_END:
					nk_input_key(ctx, NK_KEY_TEXT_END, press);
					nk_input_key(ctx, NK_KEY_SCROLL_END, press);
					break;
				case GLFW_KEY_PAGE_DOWN:
					nk_input_key(ctx, NK_KEY_SCROLL_DOWN, press);
					break;
				case GLFW_KEY_PAGE_UP:
					nk_input_key(ctx, NK_KEY_SCROLL_UP, press);
					break;
				case GLFW_KEY_LEFT_SHIFT:
				case GLFW_KEY_RIGHT_SHIFT:
					nk_input_key(ctx, NK_KEY_SHIFT, press);
					break;
				case GLFW_KEY_LEFT_CONTROL:
				case GLFW_KEY_RIGHT_CONTROL:
					if (press) {
						nk_input_key(ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
					} else {
						nk_input_key(ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_COPY, false);
						nk_input_key(ctx, NK_KEY_PASTE, false);
						nk_input_key(ctx, NK_KEY_CUT, false);
						nk_input_key(ctx, NK_KEY_SHIFT, false);
					}
					break;
				}
			}

		});


		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
                Mouse.getInstance().update_buttons(button, action);
                NkContext ctx = NuklearContext.getInstance().ctx;
                try (MemoryStack stack = stackPush()) {
                    DoubleBuffer cx = stack.mallocDouble(1);
                    DoubleBuffer cy = stack.mallocDouble(1);

                    glfwGetCursorPos(window, cx, cy);

                    int x = (int)cx.get(0);
                    int y = (int)cy.get(0);

                    int nkButton;
                    switch (button) {
                        case GLFW_MOUSE_BUTTON_RIGHT:
                            nkButton = NK_BUTTON_RIGHT;
                            break;
                        case GLFW_MOUSE_BUTTON_MIDDLE:
                            nkButton = NK_BUTTON_MIDDLE;
                            break;
                        default:
                            nkButton = NK_BUTTON_LEFT;
                    }
                    nk_input_button(ctx, nkButton, x, y, action == GLFW_PRESS);
                }
			}

		});

		glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {

            @Override
            public void invoke(long window, double xpos, double ypos) {
                Mouse.getInstance().update_position(xpos, ypos);
                nk_input_motion(NuklearContext.getInstance().ctx, (int) xpos, (int) ypos);
            }

        });

		glfwSetScrollCallback(window, (window, xOffset, yOffset) -> {
			try (MemoryStack stack = stackPush()) {
				NkVec2 scroll = NkVec2.mallocStack(stack)
						.x((float) xOffset)
						.y((float) yOffset);
				nk_input_scroll(NuklearContext.getInstance().ctx, scroll);
			}
		});

		glfwSetCharCallback(window, (window, code) -> {

                nk_input_unicode(NuklearContext.getInstance().ctx, code);

        });

		glfwSwapInterval(vsync ? 1 : 0);

		createCapabilities();

		glEnable(GL_MULTISAMPLE);

		glfwSetInputMode(window, GLFW_CURSOR, vismouse ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);

		frameRateCounter = new FrameRateCounter();
        instance = this;

		Loader.create();
		ImageRenderer gr = new ImageRenderer();
		BufferedImage image = ResourceLoader.loadImage("/logo.png");
		int textureid = Loader.getInstance().loadTexture(image);

		gr.render(textureid);
		gr.dispose();
		glfwSwapBuffers(window);

		AssetCache.load();
	}

	public void toggleView() {
	    if (!fullscreen) {
            GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowMonitor(window, NULL, 0, 0, mode.width(), mode.height(), mode.refreshRate());
        }
        else {
            glfwSetWindowSize(window, width, height);
            glfwSetWindowPos(window, 0, 0);
            glfwRestoreWindow(window);
            glfwMaximizeWindow(window);
        }
        fullscreen = !fullscreen;
    }

	/**
	 * Sets window position
	 * 
	 * @param x		x position
	 * @param y		y position
	 */
	public void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(window, x, y);
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
		NuklearContext.getInstance().newFrame();
		NuklearContext.getInstance().layout();
		NuklearContext.getInstance().render();
		glfwSwapBuffers(window);
		frameRateCounter.calculate();
	}

	/**
	 * Releases all callbacks and disposes of the window
	 */
	public void close() {
	    cursorPosCallback.free();
		keyCallback.free();
		mouseButtonCallback.free();
		framebufferCallback.free();
		windowSizeCallback.free();
		errorCallback.free();
		glfwTerminate();
		instance = null;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
		return (float) Mouse.getInstance().getDX();
	}

	/**
	 * Gets the movement of the mouse along the Y axis
	 * 
	 * @return Value of the delta y in the mouse movement
	 */
	public float getDY() {
		return (float) Mouse.getInstance().getDY();
	}

	/**
	 * Gets the mouse's X coordinate
	 * 
	 * @return Value of the x position of the mouse
	 */
	public float getX() {
		return (float) Mouse.getInstance().getX();
	}

	/**
	 * Gets the mouse's Y coordinate
	 * 
	 * @return Value of the y position of the mouse
	 */
	public float getY() {
		return (float) Mouse.getInstance().getY();
	}

	/**
	 * Primes the FPS counter
	 * 
	 * @throws Exception  thrown if GLFW is not initialized
	 */
	public void primeFPSCounter() throws Exception {
		frameRateCounter.start = GLFW.glfwGetTime();
	}

	/**
	 * Takes a screenshot of the front buffer
	 * 
	 * @param output			Output file location
	 * @param format			File format
	 */
	public void takeScreenshot(String output, String format) {
		glReadBuffer(GL_FRONT);
		int width = Application.getValue("WIDTH");
		int height = Application.getValue("HEIGHT");
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		Runnable capture = () -> {
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

			try {
				ImageIO.write(image, format, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		new Thread(capture).start();

	}

	/**
	 * Makes window visible and focuses window
	 */
	public void showWindow() {
		glfwShowWindow(window);
		glfwFocusWindow(window);
	}

	/**
	 * Gets string representation of window
	 * 
	 * @return String representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Window:");
		sb.append("\nWidth:\t" + width + "\tHeight: " + height);
		sb.append("\tVSYNC:\t" + vsync);
		sb.append("\nVersion:\t\t" + glGetString(GL_VERSION));
		sb.append("\nRenderer:\t" + glGetString(GL_RENDERER));
		sb.append("\nVendor:\t\t" + glGetString(GL_VENDOR));

		return sb.toString();
	}

	public static boolean isKeyDown(int keycode) {
		return Keyboard.getInstance().isKeyDown(keycode);
	}
}

class FrameRateCounter {

	public double start;
	private double delta;
	private double end;
	protected short frames = 0;
	protected int fps = 100;

	/**
	 * Constructor of the frame rate counter
	 */
	public FrameRateCounter() {

	}

	/**
	 * Starts the frame rate counter.  Called at beginning of each GLFW update
	 */
	public void start() throws Exception {
		if(glfwInit()) {
			delta = GLFW.glfwGetTime();
		} else {
			throw new Exception("GLFW NOT INITIALISED");
		}
	}

	/**
	 * Ends the frame time counter and calculates.  Called at end of each GLFW update
	 */
	public void calculate() throws Exception {
		if(glfwInit()) {
			end = GLFW.glfwGetTime();
		} else {
			throw new Exception("GLFW NOT INITIALISED");
		}
		delta = end - delta;
		if(end - start >= 1) {
			fps = frames;
			frames = 0;
			start = GLFW.glfwGetTime();
			System.out.println(fps);
		}
		frames++;

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
		return 1.0f / fps;
	}

}
