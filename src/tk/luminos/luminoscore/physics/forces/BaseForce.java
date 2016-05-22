package tk.luminos.luminoscore.physics.forces;

import org.lwjgl.util.vector.Vector3f;

import tk.luminos.luminoscore.graphics.display.GLFWWindow;
import tk.luminos.luminoscore.graphics.gameobjects.Entity;

/**
 * 
 * Basic force class
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class BaseForce implements Force {

	private Vector3f force;
	private Entity entity;
	
	/**
	 * Constructor
	 * 
	 * @param entity	Entity to add force to
	 * @param force		Vector representing force
	 */
	public BaseForce(Entity entity, Vector3f force) {
		this.force = force;
		this.entity = entity;
		entity.addForce(this);
	}
	
	/**
	 * Calculates the updated position of the entity
	 * 
	 * @param window	{@link GLFWWindow} to calculate frametime with 
	 */
	public Vector3f calculate(GLFWWindow window) {
		Vector3f updatedPos = entity.getPosition();
		updatedPos.x += (window.getFrameTime() * force.x);
		updatedPos.y += (window.getFrameTime() * force.y);
		updatedPos.z += (window.getFrameTime() * force.z);
		return updatedPos;
	}

}
