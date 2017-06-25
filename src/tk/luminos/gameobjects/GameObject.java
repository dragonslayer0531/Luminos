package tk.luminos.gameobjects;

import java.util.List;

import tk.luminos.graphics.SceneObject;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.maths.Vector3;
import tk.luminos.serialization.DBObject;
import tk.luminos.serialization.DBObjectType;
import tk.luminos.serialization.Serializable;

/**
 * 
 * Base class for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GameObject extends ComponentEntity implements Serializable<DBObject>, SceneObject {
	
	private boolean isRenderable;
	private float renderDistance;
	private Transformation transform;
	private String id = "DEFAULT";
	
	private List<GameObject> children = null;
	private GameObject parent = null;
		
	/**
	 * Creates new game object
	 * 
	 * @param model		model to render
	 * @param position	position of object
	 * @param rotation	rotation of object
	 * @param scale		scale of object
	 */
	public GameObject(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale) {
		this.addComponent("model", new Model(model));
		transform = new Transformation(position, rotation, scale);
		transform.constructModelMatrix();
		isRenderable = true;
	}
	
	/**
	 * Creates new game object
	 * 
	 * @param model		model to render
	 * @param position	position of object
	 * @param rotation	rotation of object
	 * @param scale		scale of object
	 * @param children	children of object
	 */
	public GameObject(TexturedModel model, Vector3 position, Vector3 rotation, Vector3 scale, List<GameObject> children) {
		this.addComponent("model", new Model(model));
		transform = new Transformation(position, rotation, scale);
		transform.constructModelMatrix();
		isRenderable = true;
		this.children = children;
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
	public final Transformation getTransformation() {
		return transform;
	}
	
	/**
	 * Gets model
	 * 
	 * @return	model
	 */
	public final TexturedModel getModel() {
		return (TexturedModel) this.getComponent("model").getComponent();
	}
	
	/**
	 * Gets transformation position
	 * 
	 * @return	position
	 */
	public final Vector3 getPosition() {
		if (parent == null)
			return transform.position;
		else 
			return Vector3.add(transform.position, parent.transform.position, null);
	}
	
	/**
	 * Gets transformation rotation
	 * 
	 * @return	rotation
	 */
	public final Vector3 getRotation() {
		if (parent == null)
			return transform.rotation;
		else 
			return Vector3.add(transform.rotation, parent.transform.rotation, null);
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
	 * Gets the children
	 * 
	 * @return the children
	 */
	public List<GameObject> getChildren() {
		return children;
	}

	/**
	 * Gets the parent GameObject
	 * 
	 * @return the parent
	 */
	public GameObject getParent() {
		return parent;
	}
	
	/**
	 * Adds a child game object
	 * 
	 * @parma child		child to add
	 */
	public void addChild(GameObject child) {
		this.children.add(child);
		child.parent = this;
	}
	
	/**
	 * Removes a child game object
	 * 
	 * @param child		child to remove
	 */
	public void removeChild(GameObject child) {
		this.children.remove(child);
		child.parent = null;
	}
	
	/**
	 * Default update method: does nothing
	 */
	public void update() {
		// DEFAULT UPDATE METHOD: DOES NOTHING
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
		return other.getModel().equals(this.getModel()) && other.getTransformation().getComponent().equals(this.getTransformation().getComponent());
	}
	
	public DBObject serialize(String name) {
		DBObject object = new DBObject(name, DBObjectType.GAMEOBJECT);
		object.addArray(transform.position.serialize("position"));
		object.addArray(transform.rotation.serialize("rotation"));
		object.addArray(transform.scale.serialize("scale"));
		return object;
	}
	
}
