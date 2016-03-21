package luminoscore.graphics.entities;

import java.util.HashMap;

import luminoscore.graphics.entities.components.Camera;
import luminoscore.graphics.entities.components.Component;
import luminoscore.graphics.entities.components.ComponentException;
import luminoscore.graphics.entities.components.Velocity;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.util.math.vector.Vector3f;

public class Entity {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Component Mapping	
	private HashMap<Class<?>, Component> components;
	private Camera camera;
	
	//Constructor Fields
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private TexturedModel model;
	
	/*
	 * @param position Defines the entity's position
	 * @param rotation Defines the entity's rotation
	 * @param scale Defines the scale of the entity
	 * @param model Defines the model to be rendered
	 * 
	 * Constructor
	 */
	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel model) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
	}
	
	/*
	 * @param c Component to be added
	 * @throws ComponentException
	 * 
	 * Adds an Entity Component to the HashMap
	 */
	public void addComponent(Component c) throws ComponentException {
		if(components.containsKey(c.getClass())) {
			throw new ComponentException("Component " + c + " is already instantiated in the object.  "
					+ "Update the component or choose a different component type.");
		} else {
			components.put(c.getClass(), c);
		}
	}
	
	/*
	 * @param c Component to be updated
	 * @throws ComponentException
	 * 
	 * Updates an Entity Component in the HashMap
	 */
	public void updateComponent(Component c) throws ComponentException {
		if(components.containsKey(c.getClass())) {
			components.get(c.getClass()).setComponent(c);
		} else {
			addComponent(c);
		}
	}
	
	/*
	 * @param c Component to be deleted
	 * @throws ComponentException
	 * 
	 * Deletes an Entity Component from the HashMap
	 */
	public void deleteComponent(Component c) throws ComponentException {
		if(components.containsKey(c.getClass())) {
			components.remove(c.getClass(), c);
		} else {
			throw new ComponentException("Component " + c + " is not a part of this object.  ");
		}
	}
	
	/*
	 * @param c Class of the component to return the value of
	 * @throws ComponentException
	 * @returns Object
	 * 
	 * Returns the value of the component
	 */
	public Object getComponentValue(Class<?> c) throws ComponentException {
		if(components.containsKey(c)) {
			return components.get(c).getComponent();
		}
		
		throw new ComponentException("Component " + c + " is not a part of this object.  ");
	}
	
	public boolean hasComponent(Class<?> c) {
		if(components.containsKey(c)) {
			return true;
		}
		return false;
	}
	
	//Attaches Camera to the entity
	public void attachCamera() {
		this.camera = new Camera(this);
	}
	
	//Uses user inputs to manipulate the position and rotation of the entity, as well as the position and rotation of the camera
	public void move() {
		Vector3f original = this.position;
		camera.move();
		//translate
		//rotate
		if(this.hasComponent(Velocity.class))
			try {
				this.updateComponent(new Velocity(Vector3f.sub(this.position, original)));
			} catch (ComponentException e) {
				e.printStackTrace();
			}
	}
 	
	//Getter-Setter Methods
	public HashMap<Class<?>, Component> getComponents() {
		return components;
	}

	public void setComponents(HashMap<Class<?>, Component> components) {
		this.components = components;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

}
