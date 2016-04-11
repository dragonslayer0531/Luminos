package luminoscore.graphics.particles;

import org.lwjgl.util.vector.Vector3f;

import luminoscore.graphics.display.GLFWWindow;
import luminoscore.graphics.entities.Entity;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 *
 * Describes 2D particle used for particle effects
 * 
 */

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float grav;
	private float life;
	private float rotation;
	private float scale;
	
	private float elapsedTime = 0;

	/**
	 * @param position		Describes the original position
	 * @param velocity		Describes the original velocity
	 * @param grav			Describes the gravitational effect
	 * @param life			Describes the lifespan of the particle
	 * @param rotation		Describes the original rotation
	 * @param scale			Describes the original scale
	 * 
	 * Constructor
	 */
	public Particle(Vector3f position, Vector3f velocity, float grav, float life, float rotation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.grav = grav;
		this.life = life;
		this.rotation = rotation;
		this.scale = scale;
	}

	/**
	 * @param window	Window to get the frame time of
	 * @return boolean 	Whether the particle is alive
	 * 
	 * Updates particle position and velocity
	 */
	public boolean update(GLFWWindow window) {
		velocity.y += Entity.GRAVITY * grav * window.getFrameTime();
		Vector3f change = new Vector3f(velocity);
		change.scale(window.getFrameTime());
		Vector3f.add(change, position, position);
		elapsedTime += window.getFrameTime();
		return elapsedTime < life;
	}
	
	/**
	 * @return Vector3f 	Vector describing world position
	 * 
	 * Gets particles's position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return Vector3f 	Vector describing the velocity
	 * 
	 * Get's particles
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * @return float		Float describing size of particle
	 * 
	 * Gets particle's scale
	 */
	public float getScale() {
		return scale;
	}	

}
