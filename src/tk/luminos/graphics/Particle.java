package tk.luminos.graphics;

import tk.luminos.display.Window;
import tk.luminos.maths.Vector2;
import tk.luminos.maths.Vector3;

/**
 * 
 * Describes 2D particle used for particle effects
 *
 * @author Nick Clark
 * @version 1.0
 * 
 */

public class Particle {
	
	private Vector3 position;
	private Vector3 velocity;
	private float grav;
	private float life;
	private float rotation;
	private float scale;
	private ParticleTexture texture;
	private float elapsedTime = 0;
	
	private Vector2 offsetOne = new Vector2();
	private Vector2 offsetTwo = new Vector2();
	private float blend;

	public Vector2 getOffsetOne() {
		return offsetOne;
	}

	public Vector2 getOffsetTwo() {
		return offsetTwo;
	}

	public float getBlend() {
		return blend;
	}

	/**
	 * Constructor
	 * 
	 * @param texture		GPU index of the {@link ParticleTexture}
	 * @param position		Describes the original position
	 * @param velocity		Describes the original velocity
	 * @param grav			Describes the gravitational effect
	 * @param life			Describes the lifespan of the particle
	 * @param rotation		Describes the original rotation
	 * @param scale			Describes the original scale
	 */
	public Particle(ParticleTexture texture, Vector3 position, Vector3 velocity, float grav, float life, float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.grav = grav;
		this.life = life;
		this.rotation = rotation;
		this.scale = scale;
	}

	/**
	 * Gets the particle's velocity
	 * 
	 * @return Velocity of particle
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * Gets the particle's gravitational proportion
	 * 
	 * @return gravitational effect
	 */
	public float getGrav() {
		return grav;
	}

	/**
	 * Gets the particle's life span
	 * 
	 * @return Life span of particle
	 */
	public float getLife() {
		return life;
	}

	
	/**
	 * Gets the texture of the particle
	 * 
	 * @return Particle's texture
	 */
	public ParticleTexture getTexture() {
		return texture;
	}

	/**
	 * Gets the time the particle has been alive
	 * 
	 * @return Time particle has been alive
	 */
	public float getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Updates particle position and velocity
	 * 
	 * @param window	{@link Window} to get the frame time of
	 * @return boolean 	Whether the particle is alive
	 */
	public boolean update(Window window) {
		velocity.y += 50 * grav * window.getFrameTime();
		Vector3 change = new Vector3(velocity.x, velocity.y, velocity.z);
		change.scale(window.getFrameTime());
		Vector3.add(change, position, position);
		updateTextureCoordInfo();
		elapsedTime += window.getFrameTime();
		return elapsedTime < life;
	}
	
	/**
	 * Gets particles's position
	 * 
	 * @return Vector describing world position 
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Get's particles rotation
	 * 
	 * @return Vector describing the velocity
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Gets particle's scale
	 * 
	 * @return Float describing size of particle
	 * 
	 */
	public float getScale() {
		return scale;
	}

//***************************************Private Methods**************************************//
	
	/**
	 * Updates the texture coordinates of the particle
	 */
	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / life;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(offsetOne, index1);
		setTextureOffset(offsetTwo, index2);
	}
	
	/**
	 * Sets the texture offsets
	 * 
	 * @param offset	Previous offset
	 * @param index		Current position
	 */
	private void setTextureOffset(Vector2 offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

}
