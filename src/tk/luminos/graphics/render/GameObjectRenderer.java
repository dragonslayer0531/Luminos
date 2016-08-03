package tk.luminos.graphics.render;

import java.util.Map;

import tk.luminos.graphics.gameobjects.GameObject;
import tk.luminos.graphics.shaders.ShaderProgram;
import tk.luminos.maths.matrix.Matrix4f;

/**
 * 
 * Abstract class containing all data necessary for a game object renderer
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class GameObjectRenderer {
	
	private ShaderProgram shader;
	private Matrix4f projectionMatrix;
		
	/**
	 * Constructor
	 * 
	 * @param shader				Shader program to render through
	 * @param projectionMatrix		Projection matrix to render with
	 */
	public GameObjectRenderer(ShaderProgram shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		this.projectionMatrix = projectionMatrix;
	}
	
	/**
	 * Gets the renderer's shader program
	 * 
	 * @return		Shader program
	 */
	public ShaderProgram getShaderProgram() {
		return shader;
	}
	
	/**
	 * Gets the renderer's projection matrix
	 * 
	 * @return		Projection Matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	/**
	 * Initialize renderer
	 */
	public abstract void init();
	
	/**
	 * Render list of game objects.  Sorted by texture ID for quick rendering
	 * 
	 * @param gameObjects		Game objects sorted by texture ID
	 */
	public abstract void render(Map<Integer, GameObject> gameObjects);
	
	/**
	 * Cleans up the renderer, shader, and all attached processes
	 */
	public abstract void cleanUp();

}
