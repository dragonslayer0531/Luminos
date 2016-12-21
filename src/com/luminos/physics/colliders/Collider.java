package com.luminos.physics.colliders;

import java.util.ArrayList;
import java.util.List;

import com.luminos.graphics.gameobjects.Entity;
import com.luminos.tools.Maths;

public class Collider {
	
	private Entity parent;
	
	/**
	 * Creates collider 
	 * 
	 * @param parent	Parent entity
	 */
	public Collider(Entity parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns a copy of the collider's reference
	 * 
	 * @return	itself
	 */
	public Collider copy() {
		return this;
	}
	
	/**
	 * Gets all collisions detected
	 * 
	 * @param colliders		Colliders that can collide
	 * @return				List of colliders that are intersecting
	 */
	public List<Collider> getCollisions(List<Collider> colliders) {
		List<Collider> collisionDetected = new ArrayList<Collider>();
		for (Collider collider : colliders) {
			/*
			 * Tests if the distance between the bounding spheres is less than the sum of their radii
			 */
			if (collider instanceof BoundingSphere) {
				if (this instanceof BoundingSphere && 
						Maths.getDistance(collider.parent.getPosition(), this.parent.getPosition()) < ((BoundingSphere) collider).radius + ((BoundingSphere) this).radius) {
						collisionDetected.add(collider);
				}
				
			}
		}
		return collisionDetected;
	}
	
}
