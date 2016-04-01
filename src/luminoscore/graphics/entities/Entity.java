package luminoscore.graphics.entities;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.components.Component;
import luminoscore.graphics.models.TexturedModel;
import luminoscore.graphics.terrains.Terrain;
import luminoscore.input.Keyboard;

public class Entity {

	private Vector3f position, rotation;
	private float scale;
	private TexturedModel tm;
	private Camera camera;

	private Terrain terrain;

	public float dx, dy, dz;
	private float currentSpeed;
	private float currentTurnSpeed;
	private float upwardsSpeed;
	private boolean isInAir = false;

	private float RUN_SPEED = 40;

	public static final float GRAVITY = -50; 
	private static float JUMP_POWER = 18; 


	private float WALK_MODIFICATION = 0.5f;
	private float SPRINT_MODIFICATION = 1.5f;
	private float CROUCH_MODIFICATION = 0.75f;
	private float PRONE_MODIFICATION = 0.5f;

	private enum MOVEMENT_STATE {
		LAYING,
		CROUCHING,
		STANDING
	}

	private MOVEMENT_STATE state = MOVEMENT_STATE.STANDING;

	private HashMap<Class<?>, Component> map = new HashMap<Class<?>, Component>();

	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel tm) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.tm = tm;
	}

	public void attachCamera() {
		this.camera = new Camera(this);
	}

	/*
	 * Allows LocalUser to move on the display with WASD control set up.  This 
	 * method can be overridden by a user defined method.
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
		camera.move();
		dy = y - this.position.y;
	}

	/*
	 * Checks keyboard inputs in order to determine the desired movement
	 * and movement type by the user.
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
		
		if(Keyboard.isPressed(Keyboard.KEY_SPACE)) {
			jump();
		}
		
	}

	/*
	 * Checks the mouse inputs to determine the rotation desired by the user.
	 */

	private void rotate(Vector2f sensitivity, GLFWWindow window, boolean invert) {

		float angleChange = 0;
		float pitchChange = 0;
		angleChange = window.getDX() * 10 * sensitivity.x;
		pitchChange = window.getDY() * .3f * sensitivity.y;

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

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	public void addComponent(Component comp) {
		if(!map.containsKey(comp.getClass())) {
			this.map.put(comp.getClass(), comp);
		}
	}
	
	public boolean hasComponent(Class<?> c) {
		return map.containsKey(c);
	}
	
	public void removeComponent(Component comp) {
		if(!map.containsKey(comp.getClass())) {
			this.map.remove(comp.getClass());
		}
	}

	public Object getComponentValue(Class<?> c) {
		if(map.containsKey(c)) {
			return map.get(c).getComponent();
		}
		return null;
	}

	public Camera getCamera() {
		return camera;
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
		return tm;
	}

	public void setModel(TexturedModel tm) {
		this.tm = tm;
	}

	public HashMap<Class<?>, Component> getMap() {
		return map;
	}

	public void setMap(HashMap<Class<?>, Component> map) {
		this.map = map;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Vector3f getVelocity() {
		return new Vector3f(dx, dy, dz);
	}

	public float getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}

	public void setCurrentTurnSpeed(float currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}

	public float getUpwardsSpeed() {
		return upwardsSpeed;
	}

	public void setUpwardsSpeed(float upwardsSpeed) {
		this.upwardsSpeed = upwardsSpeed;
	}

	public boolean isInAir() {
		return isInAir;
	}

	public void setInAir(boolean isInAir) {
		this.isInAir = isInAir;
	}

	public MOVEMENT_STATE getState() {
		return state;
	}

	public void setState(MOVEMENT_STATE state) {
		this.state = state;
	}

	public static float getGravity() {
		return GRAVITY;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}
