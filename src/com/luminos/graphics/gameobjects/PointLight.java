package com.luminos.graphics.gameobjects;

import com.luminos.tools.maths.vector.Vector;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * The light is a wrapper for position, color, and attenuation
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public class PointLight implements Light {
	
	private Vector3f position;
	private Vector3f color;
	private Attenuation attenuation;
	
	/**
	 * Constructor using position and color
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 */
	public PointLight(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		this.attenuation = new Attenuation();
		this.attenuation.exponent = 1;
	}
	
	/**
	 * Constructor using position, color, and attenuation
	 * 
	 * @param position		Initial position of the light
	 * @param color			Initial color of the light
	 * @param attenuation	Initial attenuation of the light
	 */
	public PointLight(Vector3f position, Vector3f color, Attenuation attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.attenuation.exponent = 1;
	}
	
	/**
	 * Increases the position of the GameObject by delta
	 * 
	 * @param delta		amount to increase
	 */
	public void increasePosition(Vector delta) {
		Vector3f d = (Vector3f) delta;
		position.x += d.x;
		position.y += d.y;
		position.z += d.z;
	}
	
	/**
	 * Gets the attenuation of the light instance
	 * 
	 * @return Attenuation of light 
	 */
	public Vector3f getAttenuation(){
		return attenuation.getAttenuation();
	}

	/**
	 * Gets the position of the light instance
	 * 
	 * @return Position of light
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Gets the color of the light instance
	 * 
	 * @return Color of light
	 */
	public Vector3f getColor() {
		return color;
	}

}
