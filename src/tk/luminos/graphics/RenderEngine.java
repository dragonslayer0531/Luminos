package tk.luminos.graphics;

import tk.luminos.EngineComponent;
import tk.luminos.Scene;

public class RenderEngine extends EngineComponent {
	
	private SceneManager manager;

	public RenderEngine(SceneManager manager) {
		this.manager = manager;
	}

	@Override
	public void update(Scene scene) {
		scene.render(manager);
	}
	
	@Override
	public void dispose() {
		manager.dispose();
	}

}
