package tk.luminos.gameobjects;

import java.util.List;

import tk.luminos.graphics.SceneObject;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

/**
 * 
 * Interface for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public abstract class GameObject extends ComponentEntity implements SceneObject {
	
	private TexturedModel model;
	private boolean isRenderable;
	private float renderDistance;
	private Transformation transform;
		
	/**
	 * Creates new game object
	 * 
	 * @param model		model to render
	 * @param position	position of object
	 * @param rotation	rotation of object
	 * @param scale		scale of object
	 */
	public GameObject(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale) {
		this.model = model;
		transform = new Transformation(position, rotation, scale);
		transform.constructModelMatrix();
		isRenderable = true;
	}
	
	/**
	 * Sets position of object
	 * 
	 * @param position	new position
	 */
	public void setPosition(Vector3 position) {
		transform.position = position;
		transform.constructModelMatrix();
	}
	
	/**
	 * Sets rotation of object
	 * 
	 * @param rotation	new rotation
	 */
	public void setRotation(Vector3 rotation) {
		transform.rotation = rotation;
		transform.constructModelMatrix();
	}
	
	/**
	 * Sets scale of object
	 * 
	 * @param scale		new scale
	 */
	public void setScale(Vector3 scale) {
		transform.scale = scale;
		transform.constructModelMatrix();
	}
	
	/**
	 * Gets the model matrix
	 * 
	 * @return	model matrix
	 */
	public Matrix4 getModelMatrix() {
		return transform.getComponent();
	}
	
	/**
	 * Gets model
	 * 
	 * @return	model
	 */
	public TexturedModel getModel() {
		return model;
	}
	
	/**
	 * Gets transformation position
	 * 
	 * @return	position
	 */
	public Vector3 getPosition() {
		return transform.position;
	}
	
	/**
	 * Gets transformation rotation
	 * 
	 * @return	rotation
	 */
	public Vector3 getRotation() {
		return transform.rotation;
	}
	
	/**
	 * Gets transformation scale
	 * 
	 * @return	scale
	 */
	public Vector3 getScale() {
		return transform.scale;
	}
	
	/**
	 * Gets if game object is renderable
	 * 
	 * @return	is object renderable
	 */
	public boolean isRenderable() {
		return isRenderable;
	}
	
	/**
	 * Gets the maximum render distance of an object
	 * 
	 * @return		render distance
	 */
	public float getRenderDistance() {
		return renderDistance;
	}

	/**
	 * Sets the maximum render distance of an object
	 * 
	 * @param renderDistance		new render distance
	 */
	public void setRenderDistance(float renderDistance) {
		this.renderDistance = renderDistance;
	}

	/**
	 * Sets is object is renderable
	 * 
	 * @param isRenderable		is object renderable
	 */
	public void setRenderable(boolean isRenderable) {
		this.isRenderable = isRenderable;
	}
	
	/**
	 * Gets object id
	 * 
	 * @return id
	 */
	@Override
	public String getID() {
		return null;
	}
	
	/**
	 * Sets object id
	 * 
	 * @param id 	new id
	 */
	@Override
	public void setID(String id) {
		
	}

	/**
	 * Moves entity across terrains
	 * 
	 * @param terrains		Terrain list of world
	 * @param factor		Factor of movement
	 */
	public void move(List<Terrain> terrains, float factor) {
		
	}
	
}
