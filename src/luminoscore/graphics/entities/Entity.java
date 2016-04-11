package luminoscore.graphics.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.components.Component;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.terrains.Terrain;
import luminoscore.input.Keyboard;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * The entity is a wrapper for positions, rotations, scales, models, and other various data
 *
 */
public class Entity {

	private Vector3f position, rotation;
	private float scale;
	private List<TexturedModel> models;
	private Camera camera = null;

	private Terrain terrain;

	public float dx, dy, dz;
	private float currentSpeed;
	private float currentTurnSpeed;
	private float upwardsSpeed;
	private boolean isInAir = false;

	private static float RUN_SPEED = 5;

	public static final float GRAVITY = -50; 
	private static float JUMP_POWER = 9; 


	private static float WALK_MODIFICATION = 0.5f;
	private static float SPRINT_MODIFICATION = 1.5f;
	private static float CROUCH_MODIFICATION = 0.75f;
	private static float PRONE_MODIFICATION = 0.5f;

	private enum MOVEMENT_STATE {
		LAYING,
		CROUCHING,
		STANDING
	}

	private MOVEMENT_STATE state = MOVEMENT_STATE.STANDING;

	private HashMap<Class<?>, Object> map = new HashMap<Class<?>, Object>();

	/**
	 * @param position		Initial position of the entity
	 * @param rotation		Initial rotation of the entity
	 * @param scale			Initial scale of the entity
	 * @param tm			Initial TexturedModel of the entity
	 * 
	 * Constructor one of the entity
	 */
	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel tm) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale / 8;
		this.models = new ArrayList<TexturedModel>();
		models.add(tm);
	}

	/**
	 * @param position		Initial position of the entity
	 * @param rotation		Initial rotation of the entity
	 * @param scale			Initial scale of the entity
	 * @param models		Initial List of TexturedModels of the entity
	 * 
	 * Constructor two of the entity.  Used when an entity needs multiple models
	 */
	public Entity(Vector3f position, Vector3f rotation, float scale, List<TexturedModel> models) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.models = models;
	}


	/**
	 * @param terrains		List of terrains for collision detection against
	 * @param window		GLFWWindow used to determine frame time of
	 * 
	 * Moves the entity given terrain collision detection
	 */
	public void move(List<Terrain> terrains, GLFWWindow window) {
		float y = this.position.y;
		checkInputs(terrains, window);
		rotate(new Vector2f(4f, .5f), window, false);
		Vector3f.add(new Vector3f(0, window.getFrameTime() * currentTurnSpeed, 0), rotation, rotation);
		rotation.y %= 360;
		for(Terrain terrain : terrains) {
			if(terrain.isOnTerrain(this)) this.terrain = terrain;
		}
		if (position.y < terrain.getHeightOfTerrain(position.x, position.z)) {
			upwardsSpeed = 0;
			isInAir = false;
			position.y = terrain.getHeightOfTerrain(position.x, position.z);
		}
		upwardsSpeed += GRAVITY * window.getFrameTime();
		Vector3f.add(position, new Vector3f(0, upwardsSpeed * window.getFrameTime(), 0), position);
		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (position.y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			position.y = terrainHeight;
		}
		dy = y - this.position.y;
	}

	/**
	 * @param comp		Component to be added to the entity
	 * 
	 * Adds component to entity
	 */
	public void addComponent(Component comp) {
		if(!map.containsKey(comp.getClass())) {
			this.map.put(comp.getClass(), comp.getComponent());
		}
	}

	/**
	 * @param c			Class of component to be evaluated
	 * @return boolean	Evaluation of entity containing component
	 * 
	 * Evaluates if entity contains class of component
	 */
	public boolean hasComponent(Class<?> c) {
		return map.containsKey(c);
	}

	/**
	 * @param c			Class of component to be removed
	 * 
	 * Removes component from entity
	 */
	public void removeComponent(Class<?> c) {
		if(!map.containsKey(c)) {
			this.map.remove(c);
		}
	}

	/**
	 * @param c			Class of component to be evaluated
	 * @return Object	Value of component data
	 * 
	 * Gets data contained by component
	 */
	public Object getComponentValue(Class<?> c) {
		if(map.containsKey(c)) {
			return map.get(c);
		}
		return null;
	}

	/**
	 * @return			Camera attached to entity
	 * 
	 * Gets the camera attached to the entity
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @return Vector3f	Position of entity
	 * 
	 * Gets the position of the entity		
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position	Position of entity
	 * 
	 * Sets the position of the entity
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * @return Vector3f Rotation of entity
	 * 
	 * Gets the rotation of the entity
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	/**
	 * @param rotation	Rotation of entity
	 * 
	 * Sets the rotation of the entity
	 */
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return float 	Scale of entity
	 * 
	 * Gets the scale of the entity
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scale		Scale of entity
	 * 
	 * Sets the scale of the entity
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * @return List<TexturedModel> TexturedModels used by entity
	 * 
	 * Gets the textured models used by the entity
	 */
	public List<TexturedModel> getModels() {
		return models;
	}

	/**
	 * @return Terrain	Terrain on which the entity lies
	 * 
	 * Gets the terrain on which the entity lies
	 */
	public Terrain getTerrain() {
		return terrain;
	}

	/**
	 * @return Vector3f	Velocity of entity
	 * 
	 * Gets the velocity of the entity
	 */
	public Vector3f getVelocity() {
		return new Vector3f(dx, dy, dz);
	}

	/**
	 * @return float	Current speed of entity
	 * 
	 * Gets the current speed of the entity
	 */
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @return float 	Current turn speed of entity
	 * 
	 * Gets the current turn speed of the entity
	 */
	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}

	/**
	 * @return float 	Upwards speed of entity
	 * 
	 * Gets the upwards speed of the entity, or Y component of velocity
	 */
	public float getUpwardsSpeed() {
		return upwardsSpeed;
	}

	/**
	 * @return boolean 	Terrain collision status of entity
	 * 
	 * Gets whether the entity is in contact with the terrain
	 */
	public boolean isInAir() {
		return isInAir;
	}

	/**
	 * @return float 	Power of gravity
	 * 
	 * Gets the power of gravity on entities
	 */
	public static float getGravity() {
		return GRAVITY;
	}

	/**
	 * @param camera	Camera attached to entity
	 * 
	 * Gets the camera attached to the entity
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	//*******************************Private Methods*****************************//
	
	/**
	 * @param terrains	Terrains to be moved across
	 * @param window	Window to get frame time of
	 * 
	 * Checks the keyboard inputs and calculates new camera position
	 */
	private void checkInputs(List<Terrain> terrains, GLFWWindow window) {
		switch(state) {
		case STANDING:
			if(Keyboard.isDown(Keyboard.KEY_W)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION;
				} else if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION;
				} else {
					this.currentSpeed = RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_S)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = -RUN_SPEED * WALK_MODIFICATION;
				} else {
					this.currentSpeed = -RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else this.currentSpeed = 0;

			if(Keyboard.isDown(Keyboard.KEY_A)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * .5f;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * 1.5f;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_D)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * .5f;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * 1.5f;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (-distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);			
			}
			break;
		case CROUCHING:
			if(Keyboard.isDown(Keyboard.KEY_W)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * CROUCH_MODIFICATION;
				} else if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
				} else {
					this.currentSpeed = RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_S)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
				} else {
					this.currentSpeed = RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else this.currentSpeed = 0;

			if(Keyboard.isDown(Keyboard.KEY_A)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * CROUCH_MODIFICATION;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_D)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * CROUCH_MODIFICATION;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (-distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			}
			break;
		case LAYING:
			if(Keyboard.isDown(Keyboard.KEY_W)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * PRONE_MODIFICATION;
				} else if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
				} else {
					this.currentSpeed = RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_S)) {
				if(Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
				} else {
					this.currentSpeed = RUN_SPEED;
				}
				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else this.currentSpeed = 0;

			if(Keyboard.isDown(Keyboard.KEY_A)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * PRONE_MODIFICATION;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			} else if(Keyboard.isDown(Keyboard.KEY_D)) {
				if (Keyboard.isDown(Keyboard.KEY_LEFT_CONTROL)) {
					this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
				} else if (Keyboard.isDown(Keyboard.KEY_LEFT_SHIFT)) {
					this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * PRONE_MODIFICATION;
				} 
				else this.currentSpeed = RUN_SPEED;

				float distance = currentSpeed * window.getFrameTime();
				dx = (float) (-distance * Math.cos(Math.toRadians(rotation.y)));
				dz = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
				Vector3f.add(position, new Vector3f(dx, 0, dz), position);;
			}
			break;
		}

		if(Keyboard.isDown(Keyboard.KEY_SPACE)) {
			jump();
		}

	}

	/**
	 * @param sensitivity	Camera rotation sensitivity
	 * @param window		Window to get frame time and mouse of
	 * @param invert		Determines the inversion of movement on the Y-Axis
	 * 
	 * Calculates pitch of attached camera
	 */
	private void rotate(Vector2f sensitivity, GLFWWindow window, boolean invert) {

		float angleChange = 0;
		float pitchChange = 0;
		angleChange = window.getDX() * 10 * sensitivity.x;
		if(invert) {
			pitchChange = window.getDY() * .3f * -sensitivity.y;
		} else {
			pitchChange = window.getDY() * .3f * sensitivity.y;
		}

		Camera.pitch %= 360;
		if(Camera.pitch + pitchChange <= -90) Camera.pitch = -90 + (float) Math.abs(pitchChange);
		else if(Camera.pitch + pitchChange >= 90) Camera.pitch = 90 - (float) Math.abs(pitchChange);
		else if(Camera.pitch + pitchChange >= 270) Camera.pitch = 270 - (float) Math.abs(pitchChange);
		else if(Camera.pitch + pitchChange <= -270) Camera.pitch = -270 + (float) Math.abs(pitchChange);
		else {
			Camera.pitch -= pitchChange;
			this.currentTurnSpeed = -angleChange;
		}

	}

	/**
	 * Causes jump motion of entity
	 */
	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

}
