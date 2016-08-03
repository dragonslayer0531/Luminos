package tk.luminos.physics.colliders;

import tk.luminos.graphics.gameobjects.Entity;

/**
 * Interface for all colliders
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface Collider {
	
	/**
	 * Generation interface for collider 
	 * 
	 * @param entity			Entity to generate collider of
	 */
	public void generate(Entity entity);

}
