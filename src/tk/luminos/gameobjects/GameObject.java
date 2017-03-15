package tk.luminos.gameobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.luminos.graphics.SceneObject;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.matrix.Matrix4f;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Interface for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class GameObject implements SceneObject {
	
	private Transformation<Matrix4f> transform;
	private TexturedModel model;
	private boolean isRenderable;
	private float renderDistance;
	
	private Map<String, Component<?>> components = new HashMap<String, Component<?>>();
	
	public GameObject(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		transform = new Transformation<Matrix4f>(position, rotation, scale);
		transform.constructModelMatrix();
		isRenderable = true;
	}
	
	public void setPosition(Vector3f position) {
		transform.position = position;
		transform.constructModelMatrix();
	}
	
	public void setRotation(Vector3f rotation) {
		transform.rotation = rotation;
		transform.constructModelMatrix();
	}
	
	public void setScale(Vector3f scale) {
		transform.scale = scale;
		transform.constructModelMatrix();
	}
	
	public Matrix4f getModelMatrix() {
		return transform.getComponent();
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public Vector3f getPosition() {
		return transform.position;
	}
	
	public Vector3f getRotation() {
		return transform.rotation;
	}
	
	public Vector3f getScale() {
		return transform.scale;
	}
	
	public boolean isRenderable() {
		return isRenderable;
	}
	
	public float getRenderDistance() {
		return renderDistance;
	}

	public void setRenderDistance(float renderDistance) {
		this.renderDistance = renderDistance;
	}

	public void setRenderable(boolean isRenderable) {
		this.isRenderable = isRenderable;
	}

	/**
	 * Moves entity across terrains
	 * 
	 * @param terrains		Terrain list of world
	 * @param factor		Factor of movement
	 */
	public abstract void move(List<Terrain> terrains, float factor);
	
	/**
	 * Gets the map of components associated with the GameObject
	 * 
	 * @return		Map of components attached to entity
	 */
	public Map<String, Component<?>> getComponents() {
		return components;
	}
}
