package com.luminos;

import com.luminos.graphics.gameobjects.Entity;
import com.luminos.graphics.gameobjects.GameObject;
import com.luminos.graphics.terrains.Terrain;

/**
 * 
 * Luminos Object Utilities
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class LuminosUtils {
	
	/**
	 * Checks if the object is initialized
	 * 
	 * @param object		Object to check if it is initialized
	 * @return				If object is initialized
	 */
	public static boolean isInitialized(GameObject object) {
		return object == null;
	}
	
	/**
	 * Creates copy of entity 
	 * 
	 * @param entity	Entity to copy
	 * @return			Duplicate of entity
	 */
	public static Entity copy(Entity entity) {
		Entity ent = new Entity(entity.getPosition(), entity.getRotation(), entity.getScale(), entity.getModels());
		ent.setCamera(entity.getCamera());
		ent.setCollider(entity.getCollider());
		ent.setRenderable(entity.isRenderable());
		ent.setVelocity(entity.getVelocity());
		return ent;
	}
	
	/**
	 * Creates copy of terrain 
	 * 
	 * @param entity	Terrain to copy
	 * @return			Duplicate of terrain
	 */
	public static Terrain copy(Terrain terrain) {
		Terrain ter = new Terrain(terrain.getRawModel(), terrain.getHeights(), terrain.getTexturePack(), terrain.getBlendMap(), terrain.getX(), terrain.getZ());
		return ter;
	}
	
	/**
	 * Creates reference object of a {@link GameObject}
	 * 
	 * @param object		GameObject to reference
	 * @return				Reference of object
	 */
	public static GameObject copyReference(GameObject object) {
		GameObject obj = object;
		return obj;
	}

}
