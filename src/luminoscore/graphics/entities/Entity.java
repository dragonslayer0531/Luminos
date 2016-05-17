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
import luminoscore.input.XBOXController;

/**
 * The entity is a wrapper for positions, rotations, scales, models, and other various data
 * 
 * @author Nick Clark
 * @version 1.0
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
	private static float SPRINT_MODIFICATION = 1.75f;
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
	 * Constructor one of the entity
	 * 
	 * @param position		Initial position of the entity
	 * @param rotation		Initial rotation of the entity
	 * @param scale			Initial scale of the entity
	 * @param tm			Initial TexturedModel of the entity
	 * 
	 */
	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel tm) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale / 8;
		this.models = new ArrayList<TexturedModel>();
		models.add(tm);
	}

	/**
	 * Constructor two of the entity.  Used when an entity needs multiple models
	 * 
	 * @param position		Initial position of the entity
	 * @param rotation		Initial rotation of the entity
	 * @param scale			Initial scale of the entity
	 * @param models		Initial List of {@link TexturedModel}s of the entity
	 */
	public Entity(Vector3f position, Vector3f rotation, float scale, List<TexturedModel> models) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.models = models;
	}


	/**
	 * Moves the entity given terrain collision detection
	 * 
	 * @param terrains		List of {@link Terrain} for collision detection against
	 * @param window		GLFWWindow used to determine frame time of
	 */
	public void move(List<Terrain> terrains, GLFWWindow window) {
		float y = this.position.y;
		checkInputs(terrains, window);
		rotate(new Vector2f(5f, 5f), window, false);
		Vector3f.add(new Vector3f(0, window.getFrameTime() * currentTurnSpeed, 0), rotation, rotation);
		rotation.y %= 360;
		for(Terrain terrain : terrains) {
			if(terrain.isOnTerrain(this)) this.terrain = terrain;
		}

		upwardsSpeed += GRAVITY * window.getFrameTime();
		Vector3f.add(position, new Vector3f(0, upwardsSpeed * window.getFrameTime(), 0), position);
		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (position.y < terrainHeight || position.y <= 0 || !isInAir) {
			upwardsSpeed = 0;
			isInAir = false;
			position.y = terrainHeight;
		}
		dy = y - this.position.y;
		
		if(position.y < 0) {
			position.y = 0;
		}
	}

	/**
	 * Adds {@link Component} to entity's hashmap
	 * 
	 * @param comp		Component to be added to the entity
	 */
	public void addComponent(Component comp) {
		if(!map.containsKey(comp.getClass())) {
			this.map.put(comp.getClass(), comp.getComponent());
		}
	}

	/**
	 * Evaluates if entity contains class of {@link Component}
	 * 
	 * @param c			Class of component to be evaluated
	 * @return Evaluation of entity containing component
	 */
	public boolean hasComponent(Class<?> c) {
		return map.containsKey(c);
	}

	/**
	 * Removes {@link Component} from entity
	 * 
	 * @param c			Class of component to be removed
	 */
	public void removeComponent(Class<?> c) {
		if(!map.containsKey(c)) {
			this.map.remove(c);
		}
	}

	/**
	 * Gets data contained by component
	 * 
	 * @param c			Class of component to be evaluated
	 * @return Value of component data
	 */
	public Object getComponentValue(Class<?> c) {
		if(hasComponent(c)) {
			return map.get(c);
		}
		return null;
	}

	/**
	 * Gets the camera attached to the entity
	 * 
	 * @return	Camera attached to entity
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Gets the position of the entity		
	 * 
	 * @return Position of entity
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Sets the position of the entity
	 * 
	 * @param position	Position of entity
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Gets the rotation of the entity
	 * 
	 * @return Rotation of entity
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of the entity
	 * 
	 * @param rotation	Rotation of entity
	 */
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	/**
	 * Gets the scale of the entity
	 * 
	 * @return Scale of entity
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
	 * Gets the List of {@link TexturedModel}s used by the entity
	 * 
	 * @return TexturedModels used by entity
	 */
	public List<TexturedModel> getModels() {
		return models;
	}

	/**
	 * Gets the terrain on which the entity lies
	 * 
	 * @return Terrain on which the entity lies
	 */
	public Terrain getTerrain() {
		return terrain;
	}

	/**
	 * Gets the velocity of the entity
	 * 
	 * @return Velocity of entity
	 */
	public Vector3f getVelocity() {
		return new Vector3f(dx, dy, dz);
	}

	/**
	 * Gets the current speed of the entity
	 * 
	 * @return Current speed of entity
	 */
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * Gets the current turn speed of the entity
	 * 
	 * @return Current turn speed of entity
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
	 * Gets the {@link Camera} attached to the entity
	 * 
	 * @param camera	Camera attached to entity
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	//*******************************Private Methods*****************************//

	/**
	 * Checks the keyboard inputs and calculates new camera position
	 * 
	 * @param terrains	Terrains to be moved across
	 * @param window	Window to get frame time of
	 */
	private void checkInputs(List<Terrain> terrains, GLFWWindow window) {

		if(Keyboard.isDown(Keyboard.KEY_SPACE)) {
			jump();
		}

		if(!XBOXController.isControllerConnected()) {
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
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				}
				break;
			}
			
		} else {
			currentSpeed = XBOXController.getVerticalAxis() * RUN_SPEED;
			float distance = currentSpeed * window.getFrameTime();
			if(XBOXController.isButtonDown(XBOXController.XBOX_LEFT_STICK)) distance *= SPRINT_MODIFICATION;
			dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
			dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
			Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			currentSpeed = XBOXController.getHorizontalAxis() * RUN_SPEED;
			distance = -currentSpeed * window.getFrameTime();
			dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
			dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
			Vector3f.add(position, new Vector3f(dx, 0, dz), position);
			
			if(XBOXController.isButtonDown(XBOXController.XBOX_A)) jump();
		}

	}

	/**
	 * Calculates pitch of attached camera
	 * 
	 * @param sensitivity	Camera rotation sensitivity
	 * @param window		Window to get frame time and mouse of
	 * @param invert		Determines the inversion of movement on the Y-Axis
	 */
	private void rotate(Vector2f sensitivity, GLFWWindow window, boolean invert) {

		if(!XBOXController.isControllerConnected()) {
			float angleChange = 0;
			float pitchChange = 0;
			angleChange = window.getDX() * 7.5f * sensitivity.x;
			if(invert) {
				pitchChange = window.getDY() * .05f * -sensitivity.y;
			} else {
				pitchChange = window.getDY() * .05f * sensitivity.y;
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
		} else {
			float angleChange = 0;
			float pitchChange = 0;
			angleChange = XBOXController.getHorizontalAxisLook() * 80 * sensitivity.x;
			pitchChange = XBOXController.getVerticalAxisLook() * .5f * sensitivity.y;
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
