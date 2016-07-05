package tk.luminos.luminoscore.graphics.gameobjects;

import static tk.luminos.luminoscore.ConfigData.BACKWARD;
import static tk.luminos.luminoscore.ConfigData.FORWARD;
import static tk.luminos.luminoscore.ConfigData.JUMP;
import static tk.luminos.luminoscore.ConfigData.LEFT;
import static tk.luminos.luminoscore.ConfigData.RIGHT;
import static tk.luminos.luminoscore.ConfigData.SPRINT;
import static tk.luminos.luminoscore.ConfigData.WALK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.AssetHolder;
import tk.luminos.luminoscore.graphics.display.GLFWWindow;
import tk.luminos.luminoscore.graphics.gameobjects.components.Component;
import tk.luminos.luminoscore.graphics.models.Mesh;
import tk.luminos.luminoscore.graphics.models.RawModel;
import tk.luminos.luminoscore.graphics.models.TexturedModel;
import tk.luminos.luminoscore.graphics.terrains.Terrain;
import tk.luminos.luminoscore.graphics.textures.ModelTexture;
import tk.luminos.luminoscore.input.Keyboard;
import tk.luminos.luminoscore.input.XBOXController;
import tk.luminos.luminoscore.physics.colliders.Collider;
import tk.luminos.luminoscore.physics.forces.Force;
import tk.luminos.luminosutils.serialization.LArray;
import tk.luminos.luminosutils.serialization.LDatabase;
import tk.luminos.luminosutils.serialization.LField;
import tk.luminos.luminosutils.serialization.LObject;
import tk.luminos.luminosutils.serialization.LString;

/**
 * The entity is a wrapper for positions, rotations, scales, models, and other various data
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class Entity implements GameObject {

	private Vector3f position, rotation;
	private float scale;
	private List<TexturedModel> models;
	private Camera camera = null;
	private int curModel = 0;

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
	private Collider collider = null;
	private List<Force> forces = new ArrayList<Force>();
	
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
		this.scale = scale / 8;
		this.models = models;
	}
	
	/**
	 * Increases the position of the GameObject by delta
	 * 
	 * @param delta		amount to 
	 */
	public void increasePosition(Vector delta) {
		Vector3f d = (Vector3f) delta;
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
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
		rotate(new Vector2f(2f, 2f), window, false);
		Vector3f.add(new Vector3f(0, window.getFrameTime() * currentTurnSpeed, 0), rotation, rotation);
		rotation.y %= 360;
		for(Terrain terrain : terrains) {
			if(terrain.isOnTerrain(this)) this.terrain = terrain;
		}

		upwardsSpeed += GRAVITY * window.getFrameTime();
		Vector3f.add(position, new Vector3f(0, upwardsSpeed * window.getFrameTime(), 0), position);
		float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
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
	 * Adds {@link Force} to entity's forces list
	 * 
	 * @param f		Force to be added to the entity
	 */
	public void addForce(Force f) {
		if(!forces.contains(f)) {
			this.forces.add(f);
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
	 * Evaluates if entity contains a given {@link Force}
	 * 
	 * @param f			Force to check if entity has
	 * @return			Evaluation of entity containing force
	 */
	public boolean hasForce(Force f) {
		return forces.contains(f);
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
	 * Removes {@link Force} from entity
	 * 
	 * @param f		Force to be removed
	 */
	public void removeForce(Force f) {
		if(!forces.contains(f)) {
			this.forces.remove(f);
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
	
	/**
	 * Gets the current textured model
	 * 
	 * @return	current textured model
	 */
	public TexturedModel getCurrentModel() {
		return this.models.get(curModel);
	}
	
	/**
	 * Gets all forces applied to entity
	 * 
	 * @return	forces applied to entity
	 */
	public List<Force> getForces() {
		return this.forces;
	}
	
	/**
	 * Attaches the entity to a Luminos Object Database
	 * 
	 * @param db	Database to attach to
	 */
	public void attachToLuminosDatabase(LDatabase db) {
		db.addObject(getLuminosObject());
	}
	
	/**
	 * Gets the luminos object
	 * 
	 * @return	luminos object
	 */
	public LObject getLuminosObject() {
		LObject object = new LObject("entity");
		object.addArray(LArray.Float("pos", new float[] {position.x, position.y, position.z}));
		object.addArray(LArray.Float("rot", new float[] {rotation.x, rotation.y, rotation.z}));
		object.addField(LField.Float("sca", scale * 8));
		object.addString(LString.Create("cur_mod", AssetHolder.getValue(this.getCurrentModel().getRawModel().getVaoID())));
		object.addString(LString.Create("cur_tex", AssetHolder.getValue(this.getCurrentModel().getTexture().getID())));
		return object;
	}
	
	/**
	 * Gets bytes of the entity
	 * 
	 * @return	Byte array describing entity
	 */
	public byte[] getBytes() {
		LObject object = getLuminosObject();
		for(TexturedModel tm : models) {
			object.addString(LString.Create("mod", tm.getRawModel().getID()));
		}
		byte[] data = new byte[object.getSize()];
		object.getBytes(data, 0);
		return data;
	}
	
	/**
	 * Gets the mesh of entity
	 * 
	 * @return	Mesh of entity
	 */
	public Mesh getMesh() {
		return getCurrentModel().getRawModel().getMesh();
	}
	
	/**
	 * Attaches collider object to entity
	 * 
	 * @param collider	Collider to be attached
	 */
	public void attachCollider(Collider collider) {
		this.collider = collider;
	}
	
	/**
	 * Gets the entity's collider
	 * @return	entity's collider
	 */
	public Collider getCollider() {
		return collider;
	}
	
	/**
	 * Gets the entity's current raw model
	 * 
	 * @return 		current raw model
	 */
	public RawModel getRawModel() {
		return getCurrentModel().getRawModel();
	}
	
	/**
	 * Gets the entity's current texture 
	 * 
	 * @return 		GPU texture ID
	 */
	public int getTextureID() {
		return getCurrentModel().getTexture().getID();
	}
	
	/**
	 * Gets the entity's current model
	 * 
	 * @return 		GPU texture
	 */
	public ModelTexture getModelTexture() {
		return getCurrentModel().getTexture();
	}
	
	//*******************************Private Methods*****************************//

	/**
	 * Checks the keyboard inputs and calculates new camera position
	 * 
	 * @param terrains	Terrains to be moved across
	 * @param window	Window to get frame time of
	 */
	private void checkInputs(List<Terrain> terrains, GLFWWindow window) {

		if(!XBOXController.isControllerConnected()) {
			switch(state) {
			case STANDING:
				if(Keyboard.isDown(FORWARD)) {
					if(Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION;
					} else if(Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION;
					} else {
						this.currentSpeed = RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(BACKWARD)) {
					if(Keyboard.isDown(WALK)) {
						this.currentSpeed = -RUN_SPEED * WALK_MODIFICATION;
					} else {
						this.currentSpeed = -RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else this.currentSpeed = 0;

				if(Keyboard.isDown(LEFT)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * .5f;
					} else if (Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * 1.5f;
					} 
					else this.currentSpeed = RUN_SPEED;

					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(RIGHT)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * .5f;
					} else if (Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * 1.5f;
					} 
					else this.currentSpeed = RUN_SPEED;

					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (-distance * Math.cos(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);			
				}
				if(Keyboard.isDown(JUMP	)) {
					jump();
				}
				
				break;
			case CROUCHING:
				if(Keyboard.isDown(FORWARD)) {
					if(Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * CROUCH_MODIFICATION;
					} else if(Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
					} else {
						this.currentSpeed = RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(BACKWARD)) {
					if(Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
					} else {
						this.currentSpeed = RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else this.currentSpeed = 0;

				if(Keyboard.isDown(LEFT)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
					} else if (Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * CROUCH_MODIFICATION;
					} 
					else this.currentSpeed = RUN_SPEED;

					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(Keyboard.KEY_D)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * CROUCH_MODIFICATION;
					} else if (Keyboard.isDown(SPRINT)) {
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
				if(Keyboard.isDown(RIGHT)) {
					if(Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * PRONE_MODIFICATION;
					} else if(Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
					} else {
						this.currentSpeed = RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(Keyboard.KEY_S)) {
					if(Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
					} else {
						this.currentSpeed = RUN_SPEED;
					}
					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
					dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else this.currentSpeed = 0;

				if(Keyboard.isDown(LEFT)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
					} else if (Keyboard.isDown(SPRINT)) {
						this.currentSpeed = RUN_SPEED * SPRINT_MODIFICATION * PRONE_MODIFICATION;
					} 
					else this.currentSpeed = RUN_SPEED;

					float distance = currentSpeed * window.getFrameTime();
					dx = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
					dz = (float) (-distance * Math.sin(Math.toRadians(rotation.y)));
					Vector3f.add(position, new Vector3f(dx, 0, dz), position);
				} else if(Keyboard.isDown(RIGHT)) {
					if (Keyboard.isDown(WALK)) {
						this.currentSpeed = RUN_SPEED * WALK_MODIFICATION * PRONE_MODIFICATION;
					} else if (Keyboard.isDown(SPRINT)) {
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
			pitchChange = window.getDY() * .05f * -sensitivity.y;
			if(!invert) pitchChange *= -1;

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
			if(invert) pitchChange *= -1;
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
