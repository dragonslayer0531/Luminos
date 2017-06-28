package tk.luminos.benchmark;

import org.lwjgl.glfw.GLFW;
import tk.luminos.*;
import tk.luminos.display.Window;
import tk.luminos.editor.nodes.NodeEditor;
import tk.luminos.editor.shader.ShaderEditor;
import tk.luminos.graphics.render.SceneRenderer;
import tk.luminos.graphics.ui.NuklearContext;
import tk.luminos.input.Keyboard;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;
import tk.luminos.maths.Vector4;
import tk.luminos.network.Client;
import tk.luminos.network.Server;
import tk.luminos.physics.PhysicsEngine;
import tk.luminos.util.DateUtils;

import java.io.IOException;
import java.util.List;

import static tk.luminos.Engine.addPhysicsEngine;
import static tk.luminos.Engine.createEngine;
import static tk.luminos.Luminos.ExitStatus.SUCCESS;
import static tk.luminos.input.Keyboard.*;

public class Benchmark extends Application {

	public static void main(String[] args) {
		Benchmark benchmark = new Benchmark();
		
		try {
			Application.loadSettings("settings.lum");
			benchmark.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Luminos.exit(SUCCESS);
	}

	public void start() {

		Window window = null;
		try {
			window = Window.create("Luminos Test Application: OpenGL", 1920, 1080, true, false, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Debug.prepare();
			SceneOne sceneOne = SceneOne.init();

			createEngine();

			addPhysicsEngine(new PhysicsEngine(this));
			Engine.start(window);

			this.addEvent(new CloseAction(this, window));
			this.addEvent(new PingAction(this));
			this.addEvent(new FPSAction(window));
			this.addEvent(new ScreenShotAction(window));
			this.addEvent(new WireFrameAction());
			this.addEvent(new SpeedUpAction(sceneOne));
			this.addEvent(new FullscreenAction(window));

			setActiveScene(sceneOne);

			Server server = new Server(this);
			this.attachThread(server);

			Client client = new Client(this, "localhost");

			this.attachThread(client);

			client.sendData("PING".getBytes());
			
			ShaderEditor edit = new ShaderEditor(10, 10, 800, 600, "Shader Node Editor");
			NodeEditor.node_editor_add_node(edit, "Input Node", new Vector4(10, 10, 150, 150), 0, 1);
			NodeEditor.node_editor_add_node(edit, "Input Node", new Vector4(10, 200, 150, 150), 0, 1);
			NodeEditor.node_editor_add_node_vector2(edit, "Input Node", new Vector4(10, 390, 150, 150), 1, 1, new Vector2());
			NodeEditor.node_editor_add_node(edit, "Output Node", new Vector4(200, 90, 150, 150), 2, 0);
			NodeEditor.node_editor_add_node_vector3(edit, "Input Vector 3", new Vector4(200, 400, 150, 150), 1, 1, new Vector3());
			
			window.primeFPSCounter();
			NuklearContext.getInstance().add(edit);
			this.render(window);

			window.close();
			Engine.close();
			this.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class CloseAction extends Event {

	private Application app;
	private Window window;

	public CloseAction(Application app, Window window) {
		this.app = app;
		this.window = window;
	}

	@Override
	public boolean eventPerformed() {
		return Window.isKeyDown(GLFW.GLFW_KEY_ESCAPE) || window.shouldClose();
	}

	@Override
	public void act() {
		app.shouldClose = true;
	}

}

class PingAction extends Event {

	private Application app;

	public PingAction(Application app) {
		super();
		this.app = app;
	}

	@Override
	public boolean eventPerformed() {
		return Window.isKeyDown(GLFW.GLFW_KEY_P);
	}

	@Override
	public void act() {
		List<Thread> threads = app.getThreads();
		for (Thread thread : threads) {
			if (thread instanceof Client) {
				System.out.println("PING: " + ((Client) thread).getPing() + "ms");
			}
		}
	}

}

class FullscreenAction extends Event {

	private Window window;
	private long sleep = 1000;
	private long triggered;

	public FullscreenAction(Window window) {
		triggered = System.currentTimeMillis();
		this.window = window;
	}

	public boolean eventPerformed() {
		if (Keyboard.getInstance().isKeyPressed(KEY_F11) && triggered + sleep < System.currentTimeMillis()) {
			triggered = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	public void act() {
		window.toggleView();
	}

}

class FPSAction extends Event {

	private Window window;

	public FPSAction(Window window) {
		super();
		this.window = window;
	}

	@Override
	public boolean eventPerformed() {
		return Window.isKeyDown(KEY_F);
	}

	@Override
	public void act() {
		System.out.println(window.getFPS() + "fps");
	}

}

class ScreenShotAction extends Event {

	private Window window;

	public ScreenShotAction(Window window) {
		super();
		this.window = window;
	}

	@Override
	public boolean eventPerformed() {
		return Window.isKeyDown(KEY_F12);
	}

	@Override
	public void act() {
		try {
			window.takeScreenshot("screenshot" + DateUtils.getDateTime() + ".png", "PNG");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class WireFrameAction extends Event {

    private long sleep = 1000;
    private long triggered;

	public WireFrameAction() {
        triggered = System.currentTimeMillis();
	}
	
	@Override
	public boolean eventPerformed() {
		if (Window.isKeyDown(KEY_0) && triggered + sleep < System.currentTimeMillis()) {
		    triggered = System.currentTimeMillis();
		    return true;
        }
        return false;
	}
	
	@Override
	public void act() {
		SceneRenderer.WIREFRAME = !SceneRenderer.WIREFRAME;
	}
	
}

class SpeedUpAction extends Event {
	
	SceneOne scene;
	
	public SpeedUpAction(SceneOne scene) {
		this.scene = scene;
	}
	
	@Override
	public boolean eventPerformed() {
		boolean perf = Window.isKeyDown(KEY_LEFT_SHIFT);
		if (!perf) {
			scene.speedFactor = 1;
		}
		return perf;
	}
	
	@Override
	public void act() {
		scene.speedFactor = 3;
	}
	
}