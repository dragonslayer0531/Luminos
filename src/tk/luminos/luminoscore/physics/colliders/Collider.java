package tk.luminos.luminoscore.physics.colliders;

import tk.luminos.luminoscore.graphics.gameobjects.Entity;

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
