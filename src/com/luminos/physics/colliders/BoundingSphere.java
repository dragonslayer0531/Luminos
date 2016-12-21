package com.luminos.physics.colliders;

import com.luminos.graphics.gameobjects.Entity;

public class BoundingSphere extends Collider {
	
	/**
	 * Radius of bounding sphere
	 */
	public int radius = 0;
	
	/**
	 * Creates bounding sphere with a radius of 0
	 * 
	 * @param parent		Parent entity
	 */
	public BoundingSphere(Entity parent) {
		super(parent);
	}
	
	/**
	 * Creates bounding sphere with a radius
	 * 
	 * @param radius		Radius of bounds
	 * @param parent		Parent entity
	 */
	public BoundingSphere(int radius, Entity parent) {
		super(parent);
		this.radius = radius;
	}

}
