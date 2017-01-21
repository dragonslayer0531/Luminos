package tk.luminos.graphics.gameobjects;

import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Directional light for scene lighting.  Usually only
 * one is implemented into the shaders, as there is only
 * one sun.
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class DirectionalLight implements Light {
	
	private Vector3f color;
	private Vector3f direction;
	private float intensity;
	private String id;

	/**
	 * Creates directional light
	 * 
	 * @param color			Color of light
	 * @param direction		Direction of light
	 * @param intensity		Intensity of light
	 */
	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}
	
	/**
	 * Sets the direction of the light
	 * 
	 * @param direction		Direction of light
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
	/**
	 * Sets the color of the light
	 * 
	 * @param color		Color of light
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	/**
	 * Sets the intensity of the light
	 * 
	 * @param intensity		Intensity of light
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/**
	 * Gets the color of the light
	 * 
	 * @return 	Light color
	 */
	@Override
	public Vector3f getColor() {
		return color;
	}
	
	/**
	 * Gets the direction of the light
	 * 
	 * @return		Light direction
	 */
	public Vector3f getDirection() {
		return direction;
	}
	
	/**
	 * Gets the intensity of the light
	 * 
	 * @return		Light intensity
	 */
	public float getIntensity() {
		return intensity;
	}
	
	/**
	 * Sets the ID of the light
	 * 
	 * @param id	ID of light
	 */
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the ID of the light
	 * 
	 * @return		Light ID
	 */
	@Override
	public String getID() {
		return id;
	}

}
