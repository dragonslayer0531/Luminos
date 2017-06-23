package tk.luminos.graphics.render;

import java.util.List;

import tk.luminos.graphics.VertexArray;
import tk.luminos.graphics.shaders.GUIShader;
import tk.luminos.graphics.ui.GUIObject;
import tk.luminos.loaders.Loader;

/**
 * Renders GUIs to screen
 * 
 * @author Nick Clark
 * @version 1.0
 */
public class GUIRenderer {
	
	private final VertexArray quad;
	private final static float[] positions;
	private final GUIShader shader;
	
	static {
		positions = new float[] { -1, 1, -1, -1, 1, 1, 1, -1 };
	}
	
	/**
	 * Creates a new GUI Renderer
	 * 
	 * @throws Exception	Thrown if shader files are not found
	 */
	public GUIRenderer() throws Exception {
		quad = Loader.getInstance().load(positions, 2);
		shader = new GUIShader();
	}
	
	/**
	 * Renders GUI objects to screen
	 * 
	 * @param objects		GUIObjects to render
	 */
	public void render(List<GUIObject> objects) {
		objects.forEach(obj -> obj.render(shader, quad));
	}
	
	/**
	 * Disposes of GUI shader
	 */
	public void dispose() {
		shader.dispose();
	}

}
