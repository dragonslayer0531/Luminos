package tk.luminos.gameobjects;

import java.util.List;

import tk.luminos.graphics.SceneObject;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.Matrix4;
import tk.luminos.maths.Vector3;

/**
 * 
 * Base class for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GameObject extends ComponentEntity implements SceneObject {
	
	private boolean isRenderable;
	private float renderDistance;
	private Transformation transform;
	private String id = "DEFAULT";
		
	/**
	 * Creates new game object
	 * 
	 * @param model		model to render
	 * @param position	position of object
	 * @param rotation	rotation of object
	 * @param scale		scale of object
	 */
	public GameObject(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale) {
		this.addComponent(new Model(model));
		transform = new Transformation(position, rotation, scale);
		transform.constructModelMatrix();
		isRenderable = true;
	}
	
	/**
	 * Sets position of object
	 * 
	 * @param position	new position
	 */
	public final void setPosition(Vector3 position) {
		transform.position = position;
		transform.constructModelMatrix();
	}
	
	/**
	 * Sets rotation of object
	 * 
	 * @param rotation	new rotation
	 */
	public final void setRotation(Vector3 rotation) {
		transform.rotation = rotation;
		transform.constructModelMatrix();
	}
	
	/**
	 * Sets scale of object
	 * 
	 * @param scale		new scale
	 */
	public final void setScale(Vector3 scale) {
		transform.scale = scale;
		transform.constructModelMatrix();
	}
	
	/**
	 * Gets the model matrix
	 * 
	 * @return	model matrix
	 */
	public final Matrix4 getModelMatrix() {
		return transform.getComponent();
	}
	
	/**
	 * Gets model
	 * 
	 * @return	model
	 */
	public final TexturedModel getModel() {
		return (TexturedModel) this.getComponent(Model.class).getComponent();
	}
	
	/**
	 * Gets transformation position
	 * 
	 * @return	position
	 */
	public final Vector3 getPosition() {
		return transform.position;
	}
	
	/**
	 * Gets transformation rotation
	 * 
	 * @return	rotation
	 */
	public final Vector3 getRotation() {
		return transform.rotation;
	}
	
	/**
	 * Gets transformation scale
	 * 
	 * @return	scale
	 */
	public final Vector3 getScale() {
		return transform.scale;
	}
	
	/**
	 * Gets if game object is renderable
	 * 
	 * @return	is object renderable
	 */
	public final boolean isRenderable() {
		return isRenderable;
	}
	
	/**
	 * Gets the maximum render distance of an object
	 * 
	 * @return		render distance
	 */
	public final float getRenderDistance() {
		return renderDistance;
	}

	/**
	 * Sets the maximum render distance of an object
	 * 
	 * @param renderDistance		new render distance
	 */
	public final void setRenderDistance(float renderDistance) {
		this.renderDistance = renderDistance;
	}

	/**
	 * Sets is object is renderable
	 * 
	 * @param isRenderable		is object renderable
	 */
	public final void setRenderable(boolean isRenderable) {
		this.isRenderable = isRenderable;
	}
	
	/**
	 * Gets object id
	 * 
	 * @return id
	 */
	@Override
	public String getID() {
		return id;
	}
	
	/**
	 * Sets object id
	 * 
	 * @param id 	new id
	 */
	@Override
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * Moves entity across terrains
	 * 
	 * @param terrains		Terrain list of world
	 * @param factor		Factor of movement
	 */
	public void move(List<Terrain> terrains, float factor) {
		// DEFAULT MOVE METHOD: DOES NOTHING
	}
	
	/**
	 * Checks if two Game Objects are equivalent
	 * 
	 * @return				If equivalent
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof GameObject))
			return false;
		GameObject other = (GameObject) obj;
		return other.getModel().equals(this.getModel()) && other.getModelMatrix().equals(this.getModelMatrix());
	}
	
}
