package tk.luminos.luminoscore.physics.forces;

import java.util.Iterator;

import tk.luminos.luminoscore.graphics.display.GLFWWindow;
import tk.luminos.luminoscore.graphics.gameobjects.Entity;

/**
 * Manages all physics operations
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class PhysicsManager {
	
	private GLFWWindow window;
	
	/**
	 * Constructor
	 * 
	 * @param window		GLFWWindow to use in frame time calculations
	 */
	public PhysicsManager(GLFWWindow window) {
		this.window = window;
	}
	
	/**
	 * Updates the entities's positions with physics
	 * 
	 * @param entities		Entities to update
	 */
	public void update(Iterator<Entity> entities) {
		while(entities.hasNext()) {
			updateEntity(entities.next());
		}
	}
	
	/**
	 * Updates a single entity's position
	 * 
	 * @param entity		Entity to update
	 */
	public void updateEntity(Entity entity) {
		for(Force force : entity.getForces()) force.calculate(window);
	}

}
