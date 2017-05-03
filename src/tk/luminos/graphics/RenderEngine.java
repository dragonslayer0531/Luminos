package tk.luminos.graphics;

import tk.luminos.EngineComponent;
import tk.luminos.Scene;

/**
 * Generates new render engine
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class RenderEngine extends EngineComponent {
	
	private SceneManager manager;

	/**
	 * Creates new render engine
	 * 
	 * @param manager	manages scene rendering
	 */
	public RenderEngine(SceneManager manager) {
		this.manager = manager;
	}

	/**
	 * Updates viewport
	 * 
	 * @param scene		scene to render
	 */
	@Override
	public void update(Scene scene) {
		scene.render(manager);
	}
	
	/**
	 * Disposes of engine component
	 */
	public void dispose() {
		manager.dispose();
	}

}
